package org.cric.back_office.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.cric.back_office.global.service.TokenService;
import org.cric.back_office.global.util.JwtUtil;
import org.cric.back_office.user.dto.*;
import org.cric.back_office.user.entity.RefreshToken;
import org.cric.back_office.user.entity.User;
import org.cric.back_office.user.entity.UserSilo;
import org.cric.back_office.user.enums.UserStatus;
import org.cric.back_office.user.repository.RefreshTokenRepository;
import org.cric.back_office.user.repository.UserJpaRepository;
import org.cric.back_office.user.repository.UserRepository;
import org.cric.back_office.work.entity.Silo;
import org.cric.back_office.work.repository.SiloJpaRepository;
import org.cric.back_office.work.repository.SiloRepository;
import org.slf4j.ILoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserJpaRepository userJpaRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final SiloJpaRepository siloJpaRepository;

    @Transactional
    public Long saveUser(UserRegistDto userRegistDto) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userRegistDto.getPassword());
        userRegistDto.setPassword(encodedPassword);
        User user = User.createUser(userRegistDto);

        User save = userJpaRepository.save(user);
        return save.getId();
    }

    @Transactional
    public void editUser(Long id, UserEditDto userEditDto) {
        User user = userJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        user.settingEditUser(user, userEditDto);
        userJpaRepository.save(user);
    }

    @Transactional
    public void removeUser(Long id) {
        User user = userJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        user.setUserStatus(UserStatus.DELETED);
        userJpaRepository.save(user);
    }

    public UserResponseDto findUserById(Long id) {
        User user = userJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id:" + id));
        return new UserResponseDto(user);
    }

    public List<UserResponseDto> findUserByIdWithCondition(FindUserCondition condition) {
        List<User> users = userRepository.searchUserWithCondition(condition);
        ArrayList<UserResponseDto> userResponseDtoList = new ArrayList<>();
        for (User user : users) {
            userResponseDtoList.add(new UserResponseDto(user));
        }
        return userResponseDtoList;
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userJpaRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다"));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다");
        }

        if (user.getUserStatus() != UserStatus.APPROVED) {
            throw new IllegalArgumentException("승인되지 않은 사용자입니다");
        }

        // 토큰 ID 생성 (중복 로그인 방지용)
        String tokenId = jwtUtil.generateTokenId();

        // Access Token 생성 (tokenId, role 포함)
        String accessToken = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getName(), tokenId,
                user.getUserRole().name());

        // Refresh Token 생성
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        // Redis에 토큰 ID 저장 (기존 토큰 자동 무효화)
        tokenService.saveTokenId(user.getId(), tokenId, jwtUtil.getAccessTokenExpiration());

        // Refresh Token DB 저장 (기존 토큰이 있으면 업데이트, 없으면 새로 생성)
        saveOrUpdateRefreshToken(user.getId(), refreshToken);

        return new LoginResponseDto(accessToken, refreshToken, user.getUserRole());
    }

    @Transactional
    public RefreshTokenResponseDto refreshAccessToken(String refreshToken) {
        // Refresh Token 검증
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 refresh token입니다");
        }

        // DB에서 Refresh Token 조회
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 refresh token입니다"));

        // 만료 여부 확인
        if (storedToken.isExpired()) {
            throw new IllegalArgumentException("만료된 refresh token입니다. 다시 로그인해주세요.");
        }

        // 사용자 정보 조회
        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        User user = userJpaRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        // 새로운 토큰 ID 생성 (중복 로그인 방지용)
        String newTokenId = jwtUtil.generateTokenId();

        // 새로운 Access Token 생성 (tokenId, role 포함)
        String newAccessToken = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getName(), newTokenId,
                user.getUserRole().name());

        // Refresh Token Rotation (RTR): 새로운 Refresh Token 생성
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getId());
        LocalDateTime newExpiryDate = LocalDateTime.now().plusSeconds(jwtUtil.getRefreshExpiration() / 1000);

        // DB의 Refresh Token 교체 (Rotation)
        storedToken.updateToken(newRefreshToken, newExpiryDate);

        // Redis에 새 토큰 ID 저장 (기존 토큰 자동 무효화)
        tokenService.saveTokenId(user.getId(), newTokenId, jwtUtil.getAccessTokenExpiration());

        return new RefreshTokenResponseDto(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout(Integer userId) {
        // Redis에서 토큰 ID 삭제 (현재 세션 무효화)
        tokenService.removeTokenId(userId);
        // DB에서 Refresh Token 삭제
        refreshTokenRepository.deleteByUserId(userId);
    }

    private void saveOrUpdateRefreshToken(Long userId, String refreshToken) {
        LocalDateTime expiryDate = LocalDateTime.now()
                .plusSeconds(jwtUtil.getRefreshExpiration() / 1000);

        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUserId(userId);
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id:" + userId));

        if (existingToken.isPresent()) {
            // 기존 토큰 업데이트
            existingToken.get().updateToken(refreshToken, expiryDate);
        } else {
            // 새 토큰 생성
            RefreshToken newToken = RefreshToken.builder()
                    .user(user)
                    .token(refreshToken)
                    .expiryDate(expiryDate)
                    .build();
            refreshTokenRepository.save(newToken);
        }
    }

    @Transactional
    public Long changeUserStatus(Long id, String userStatus) {
        User user = userJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        user.setUserStatus(UserStatus.valueOf(userStatus));
        return user.getId();
    }

    @Transactional
    public void userAcceptSilo(Long userId, Long siloId) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        Silo silo = siloJpaRepository.findById(siloId)
                .orElseThrow(() -> new IllegalArgumentException("Silo not found with id: " + siloId));

        UserSilo userSilo = new UserSilo(user, silo);
        userRepository.acceptUserToSilo(userSilo);
    }

    public void checkUserStatusByEmail(String userEmail) {
        User user = userJpaRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("user not found with user email: " + userEmail));
        UserStatus userStatus = user.getUserStatus();
        // ai todo: 여기서 가입된 회원인지, 해당 회원이 유효한지 확인
    }

    /**
     * 초대를 위한 사용자 검증
     * 
     * @param email 검증할 사용자 이메일
     * @return 유효한 사용자면 User 엔티티 반환, 그렇지 않으면 null
     */
    public User validateUserForInvitation(String email) {
        User user = userJpaRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return null; // 사용자 없음
        }

        if (user.getUserStatus() != UserStatus.APPROVED) {
            return null; // 승인되지 않은 사용자
        }

        return user;
    }

    /**
     * ID로 User 엔티티 조회 (내부 사용)
     */
    public User getUserEntityById(Long userId) {
        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
    }
}
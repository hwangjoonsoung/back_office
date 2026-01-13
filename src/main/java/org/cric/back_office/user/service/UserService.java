package org.cric.back_office.user.service;

import lombok.RequiredArgsConstructor;

import org.cric.back_office.global.util.JwtUtil;
import org.cric.back_office.user.dto.*;
import org.cric.back_office.user.entity.RefreshToken;
import org.cric.back_office.user.entity.User;
import org.cric.back_office.user.enums.UserStatus;
import org.cric.back_office.user.repository.RefreshTokenRepository;
import org.cric.back_office.user.repository.UserJpaRepository;
import org.cric.back_office.user.repository.UserRepository;
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

    @Transactional
    public Integer saveUser(UserRegistDto userRegistDto){
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userRegistDto.getPassword());
        userRegistDto.setPassword(encodedPassword);
        User user = User.createUser(userRegistDto);

        System.out.println("임시로 회원 가입했을때 승인 상태로 변경--------------------------");
        user.setUserStatus(UserStatus.APPROVED);
        System.out.println("--------------------------");

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
        User user = userJpaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found with id:" + id));
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

        // Access Token 생성
        String accessToken = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getName());
        
        // Refresh Token 생성
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());
        
        // Refresh Token DB 저장 (기존 토큰이 있으면 업데이트, 없으면 새로 생성)
        saveOrUpdateRefreshToken(user.getId(), refreshToken);

        return new LoginResponseDto(accessToken, refreshToken);
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
            refreshTokenRepository.delete(storedToken);
            throw new IllegalArgumentException("만료된 refresh token입니다");
        }

        // 사용자 정보 조회
        Integer userId = jwtUtil.getUserIdFromToken(refreshToken);
        User user = userJpaRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        // 새로운 Access Token 생성
        String newAccessToken = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getName());

        return new RefreshTokenResponseDto(newAccessToken);
    }

    @Transactional
    public void logout(Integer userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    private void saveOrUpdateRefreshToken(Integer userId, String refreshToken) {
        LocalDateTime expiryDate = LocalDateTime.now()
                .plusSeconds(jwtUtil.getRefreshExpiration() / 1000);

        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUserId(userId);
        
        if (existingToken.isPresent()) {
            // 기존 토큰 업데이트
            existingToken.get().updateToken(refreshToken, expiryDate);
        } else {
            // 새 토큰 생성
            RefreshToken newToken = RefreshToken.builder()
                    .userId(userId)
                    .token(refreshToken)
                    .expiryDate(expiryDate)
                    .build();
            refreshTokenRepository.save(newToken);
        }
    }

    @Transactional
    public Integer changeUserStatus(Long id,String userStatus) {
        User user = userJpaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        user.setUserStatus(UserStatus.valueOf(userStatus));
        return user.getId();
    }
}
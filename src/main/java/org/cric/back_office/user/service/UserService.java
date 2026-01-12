package org.cric.back_office.user.service;

import lombok.RequiredArgsConstructor;

import org.cric.back_office.global.util.JwtUtil;
import org.cric.back_office.user.dto.*;
import org.cric.back_office.user.entity.User;
import org.cric.back_office.user.enums.UserStatus;
import org.cric.back_office.user.repository.UserJpaRepository;
import org.cric.back_office.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserJpaRepository userJpaRepository;
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
        User user = userJpaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found whth id:" + id));
        return new UserResponseDto(user);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userJpaRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다"));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다");
        }

        if (user.getUserStatus() != UserStatus.APPROVED) {
            throw new IllegalArgumentException("승인되지 않은 사용자입니다");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail());

        return new LoginResponseDto(token, "Bearer");
    }

    @Transactional
    public Integer changeUserStatus(Long id,String userStatus) {
        User user = userJpaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        user.setUserStatus(UserStatus.valueOf(userStatus));
        return user.getId();
    }
}
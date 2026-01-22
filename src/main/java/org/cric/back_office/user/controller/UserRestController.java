package org.cric.back_office.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cric.back_office.global.dto.ApiResponse;
import org.cric.back_office.user.dto.*;
import org.cric.back_office.user.enums.UserStatus;
import org.cric.back_office.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    /**
     * 로그인 API
     * POST /api/auth/login
     */
    @PostMapping("/api/auth/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponse = userService.login(loginRequestDto);
        ApiResponse<LoginResponseDto> response = new ApiResponse<>("ok", HttpStatus.OK.value(), loginResponse);
        return ResponseEntity.ok().body(response);
    }

    /**
     * Access Token 재발급 API
     * POST /api/auth/refresh
     */
    @PostMapping("/api/auth/refresh")
    public ResponseEntity<ApiResponse<RefreshTokenResponseDto>> refreshToken(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        RefreshTokenResponseDto refreshResponse = userService.refreshAccessToken(refreshTokenRequestDto.getRefreshToken());
        ApiResponse<RefreshTokenResponseDto> response = new ApiResponse<>("ok", HttpStatus.OK.value(), refreshResponse);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 로그아웃 API
     * POST /api/auth/logout
     */
    @PostMapping("/api/auth/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody LogoutRequestDto logoutRequestDto) {
        userService.logout(logoutRequestDto.getUserId());
        ApiResponse<Void> response = new ApiResponse<>("ok", HttpStatus.OK.value(), null);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 회원가입 API
     * POST /api/user
     */
    @PostMapping("/api/user")
    public ResponseEntity<ApiResponse<Integer>> registerUser(@Valid @RequestBody UserRegistDto userRegistDto) {
        Integer id = userService.saveUser(userRegistDto);
        ApiResponse<Integer> response = new ApiResponse("ok", HttpStatus.OK.value(), id);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 회원 정보 수정 API
     * PUT /api/user/{id}
     */
    @PutMapping("/api/user/{id}")
    public ResponseEntity<ApiResponse<Void>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserEditDto userEditDto) {
        userService.editUser(id, userEditDto);
        ApiResponse<Void> response = new ApiResponse("ok", HttpStatus.OK.value(), null);
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 삭제 API (상태를 DELETED로 변경)
     * DELETE /api/user/{id}
     */
    @DeleteMapping("/api/user/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.removeUser(id);
        ApiResponse<Void> response = new ApiResponse("ok", HttpStatus.OK.value(), null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 회원 등급 변경 API (상태를 변경)
     * Put /api/user/{id}/changeStatus
     */
    @PutMapping("/api/user/{id}/changeStatus")
    public ResponseEntity<ApiResponse<Void>> changeUserStatus(@PathVariable Long id,@RequestBody UserStatusDto userStatus) {
        Integer userId = userService.changeUserStatus(id, userStatus.userStatus());
        ApiResponse<Void> response = new ApiResponse("ok", HttpStatus.OK.value(), userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 회원 찾기
     * Get /api/user/{id}
     */
    @GetMapping("/api/user/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> findUserById(@PathVariable(name = "id") Long id) {
        UserResponseDto userResponseDto = userService.findUserById(id);
        ApiResponse<UserResponseDto> apiResponse = new ApiResponse<>("ok", HttpStatus.OK.value(), userResponseDto);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    /**
     * 조건 줘서 회원 찾기
     * Get /api/user/{id}
     */
    @GetMapping("/api/user/condition")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> findUserByIdWithCondition(@RequestBody FindUserCondition condition) {
        List<UserResponseDto> userByIdWithCondition = userService.findUserByIdWithCondition(condition);
        ApiResponse<List<UserResponseDto>> response = new ApiResponse<>("ok", HttpStatus.OK.value(), userByIdWithCondition);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}

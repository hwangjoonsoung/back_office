package org.cric.back_office.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cric.back_office.global.dto.ApiResponse;
import org.cric.back_office.global.dto.UserPrincipal;
import org.cric.back_office.user.dto.*;
import org.cric.back_office.user.enums.UserStatus;
import org.cric.back_office.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.ApplicationScope;

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
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto loginRequestDto,
            HttpServletResponse httpResponse) {
        LoginResponseDto loginResponse = userService.login(loginRequestDto);

        // Access Token 쿠키 설정 (HttpOnly, Secure 권장)
        Cookie accessTokenCookie = new Cookie("accessToken", loginResponse.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(60 * 60); // 1시간
        httpResponse.addCookie(accessTokenCookie);

        // Refresh Token 쿠키 설정
        Cookie refreshTokenCookie = new Cookie("refreshToken", loginResponse.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
        httpResponse.addCookie(refreshTokenCookie);

        ApiResponse<LoginResponseDto> response = new ApiResponse<>("ok", HttpStatus.OK.value(), loginResponse);
        return ResponseEntity.ok().body(response);
    }

    /**
     * Access Token 재발급 API
     * POST /api/auth/refresh
     */
    @PostMapping("/api/auth/refresh")
    public ResponseEntity<ApiResponse<RefreshTokenResponseDto>> refreshToken(
            @Valid @RequestBody RefreshTokenRequestDto refreshTokenRequestDto,
            HttpServletResponse httpResponse) {
        RefreshTokenResponseDto refreshResponse = userService
                .refreshAccessToken(refreshTokenRequestDto.getRefreshToken());

        // 새로운 Access Token 쿠키 설정
        Cookie accessTokenCookie = new Cookie("accessToken", refreshResponse.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(60 * 60); // 1시간
        httpResponse.addCookie(accessTokenCookie);

        // 새로운 Refresh Token 쿠키 설정 (RTR)
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshResponse.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
        httpResponse.addCookie(refreshTokenCookie);

        ApiResponse<RefreshTokenResponseDto> response = new ApiResponse<>("ok", HttpStatus.OK.value(), refreshResponse);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 로그아웃 API
     * POST /api/auth/logout
     */
    @PostMapping("/api/auth/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            HttpServletResponse httpResponse) {
        userService.logout(userPrincipal.getUserId());

        // 쿠키 삭제
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);
        httpResponse.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        httpResponse.addCookie(refreshTokenCookie);

        ApiResponse<Void> response = new ApiResponse<>("ok", HttpStatus.OK.value(), null);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 회원가입 API
     * POST /api/user
     */
    @PostMapping("/api/user")
    public ResponseEntity<ApiResponse<Integer>> registerUser(@Valid @RequestBody UserRegistDto userRegistDto) {
        Long id = userService.saveUser(userRegistDto);
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
    public ResponseEntity<ApiResponse<Void>> changeUserStatus(@PathVariable Long id,
                                                              @RequestBody UserStatusDto userStatus) {
        Long userId = userService.changeUserStatus(id, userStatus.userStatus());
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
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> findUserByIdWithCondition(
            @RequestBody FindUserCondition condition) {
        List<UserResponseDto> userByIdWithCondition = userService.findUserByIdWithCondition(condition);
        ApiResponse<List<UserResponseDto>> response = new ApiResponse<>("ok", HttpStatus.OK.value(),
                userByIdWithCondition);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 초대받은 링크를 클릭하면 silo 입장 권한 획득
     * Post /api/user/{id}/silo/{id}/accept
     **/
//    @PostMapping("/api/user/{userId}/silo/{siloId}/accept")
//    public ResponseEntity<Void> userAcceptSilo(@PathVariable("userId") Long userId,@PathVariable("siloId")Long siloId) {
//        userService.userAcceptSilo(userId, siloId);
//    }

    /**
     * 이메일로 정상적으로 이용 가능한 회원인지 확인
     * Get /api/user/{email}/userStatus
     **/
//    @GetMapping("/api/user/{userEmail}/userStatus")
//    public ResponseEntity<Void> checkUserStatusByEmail(@PathVariable("userEmail") String userEmail) {
//        userService.checkUserStatusByEmail(userEmail);
//    }
}

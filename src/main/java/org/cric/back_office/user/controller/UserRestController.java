package org.cric.back_office.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cric.back_office.global.dto.ApiResponse;
import org.cric.back_office.user.dto.LoginRequestDto;
import org.cric.back_office.user.dto.LoginResponseDto;
import org.cric.back_office.user.dto.UserEditDto;
import org.cric.back_office.user.dto.UserRegistDto;
import org.cric.back_office.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(response);
    }

    /**
     * 회원가입 API
     * POST /api/users
     */
    @PostMapping("/api/users")
    public ResponseEntity<ApiResponse<Void>> registerUser(@Valid @RequestBody UserRegistDto userRegistDto) {
        Integer id = userService.saveUser(userRegistDto);
        ApiResponse<Void> response = new ApiResponse("ok",HttpStatus.OK.value() ,null);
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 정보 수정 API
     * PUT /api/users/{id}
     */
    @PutMapping("/api/users/{id}")
    public ResponseEntity<ApiResponse<Void>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserEditDto userEditDto) {
        userService.editUser(id, userEditDto);
        ApiResponse<Void> response = new ApiResponse("ok",HttpStatus.OK.value() ,null);
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 삭제 API (상태를 DELETED로 변경)
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.User(id);
        ApiResponse<Void> response = new ApiResponse("ok",HttpStatus.OK.value() ,null);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

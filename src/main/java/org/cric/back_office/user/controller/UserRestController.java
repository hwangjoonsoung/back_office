package org.cric.back_office.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
     * 회원가입 API
     * POST /api/users
     */
    @PostMapping("/api/users")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserRegistDto userRegistDto) {
        userService.saveUser(userRegistDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 회원 정보 수정 API
     * PUT /api/users/{id}
     */
    @PutMapping("/api/users/{id}")
    public ResponseEntity<Void> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserEditDto userEditDto) {
        userService.editUser(id, userEditDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 회원 삭제 API (상태를 DELETED로 변경)
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.User(id);
        return ResponseEntity.noContent().build();
    }
}

package org.cric.back_office.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cric.back_office.global.config.JwtAuthenticationFilter;
import org.cric.back_office.global.util.JwtUtil;
import org.cric.back_office.user.dto.UserEditDto;
import org.cric.back_office.user.dto.UserRegistDto;
import org.cric.back_office.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = UserRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("UserRestController 테스트")
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    private UserRegistDto userRegistDto;
    private UserEditDto userEditDto;
    private Long testUserId;

    @BeforeEach
    void setUp() {
        testUserId = 1L;

        // UserRegistDto 설정
        userRegistDto = new UserRegistDto();
        userRegistDto.setEmail("test@example.com");
        userRegistDto.setPassword("password123");
        userRegistDto.setName("테스트 사용자");
        userRegistDto.setAffiliation("테스트 조직");
        userRegistDto.setPosition("테스트 직책");
        userRegistDto.setPhoneNumber("010-1234-5678");
        userRegistDto.setBirthday(LocalDate.of(1990, 1, 1));

        // UserEditDto 설정
        userEditDto = new UserEditDto();
        userEditDto.setName("수정된 이름");
        userEditDto.setAffiliation("수정된 조직");
        userEditDto.setPosition("수정된 직책");
        userEditDto.setPhoneNumber("010-9999-9999");
        userEditDto.setBirthday(LocalDate.of(1995, 5, 5));
    }

    @Test
    @DisplayName("POST /api/users - 회원가입 성공 시 200 OK를 반환한다")
    void registerUser_Success_Returns200Ok() throws Exception {
        // given
        Long savedUserId = 1L;
        when(userService.saveUser(any(UserRegistDto.class))).thenReturn(savedUserId);

        // when
        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistDto)))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        verify(userService, times(1)).saveUser(any(UserRegistDto.class));
    }

    @Test
    @DisplayName("POST /api/users - 잘못된 요청 본문 시 400 Bad Request를 반환한다")
    void registerUser_InvalidRequestBody_Returns400BadRequest() throws Exception {
        // given
        String invalidJson = "{ invalid json }";

        // when
        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
        verify(userService, never()).saveUser(any(UserRegistDto.class));
    }

    @Test
    @DisplayName("PUT /api/users/{id} - 회원 정보 수정 성공 시 200 OK를 반환한다")
    void updateUser_Success_Returns200Ok() throws Exception {
        // given
        doNothing().when(userService).editUser(eq(testUserId), any(UserEditDto.class));

        // when
        MvcResult result = mockMvc.perform(put("/api/users/{id}", testUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userEditDto)))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        verify(userService, times(1)).editUser(eq(testUserId), any(UserEditDto.class));
    }

    @Test
    @DisplayName("PUT /api/users/{id} - 존재하지 않는 사용자 수정 시 400 Internal Server Error를 반환한다")
    void updateUser_UserNotFound_Returns400InternalServerError() throws Exception {
        // given
        Long nonExistentUserId = 999L;
        doThrow(new IllegalArgumentException("User not found with id: " + nonExistentUserId))
                .when(userService).editUser(eq(nonExistentUserId), any(UserEditDto.class));

        // when
        MvcResult result = mockMvc.perform(put("/api/users/{id}", nonExistentUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userEditDto)))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
        verify(userService, times(1)).editUser(eq(nonExistentUserId), any(UserEditDto.class));
    }

    @Test
    @DisplayName("PUT /api/users/{id} - 잘못된 요청 본문 시 400 Bad Request를 반환한다")
    void updateUser_InvalidRequestBody_Returns400BadRequest() throws Exception {
        // given
        String invalidJson = "{ invalid json }";

        // when
        MvcResult result = mockMvc.perform(put("/api/users/{id}", testUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
        verify(userService, never()).editUser(any(), any(UserEditDto.class));
    }

    @Test
    @DisplayName("DELETE /api/users/{id} - 회원 삭제 성공 시 200 No Content를 반환한다")
    void deleteUser_Success_Returns204NoContent() throws Exception {
        // given
        doNothing().when(userService).removeUser(eq(testUserId));

        // when
        MvcResult result = mockMvc.perform(delete("/api/users/{id}", testUserId))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        verify(userService, times(1)).removeUser(eq(testUserId));
    }

    @Test
    @DisplayName("DELETE /api/users/{id} - 존재하지 않는 사용자 삭제 시 400 Internal Server Error를 반환한다")
    void deleteUser_UserNotFound_Returns400InternalServerError() throws Exception {
        // given
        Long nonExistentUserId = 999L;
        doThrow(new IllegalArgumentException("User not found with id: " + nonExistentUserId))
                .when(userService).removeUser(eq(nonExistentUserId));

        // when
        MvcResult result = mockMvc.perform(delete("/api/users/{id}", nonExistentUserId))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
        verify(userService, times(1)).removeUser(eq(nonExistentUserId));
    }

    @Test
    @DisplayName("POST /api/users - 요청 본문이 null인 경우 400 Bad Request를 반환한다")
    void registerUser_NullRequestBody_Returns400BadRequest() throws Exception {
        // when
        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
        verify(userService, never()).saveUser(any(UserRegistDto.class));
    }

    @Test
    @DisplayName("PUT /api/users/{id} - 요청 본문이 null인 경우 400 Bad Request를 반환한다")
    void updateUser_NullRequestBody_Returns400BadRequest() throws Exception {
        // when
        MvcResult result = mockMvc.perform(put("/api/users/{id}", testUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // then
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
        verify(userService, never()).editUser(any(), any(UserEditDto.class));
    }
}

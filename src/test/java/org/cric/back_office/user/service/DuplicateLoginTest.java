package org.cric.back_office.user.service;

import org.cric.back_office.global.service.TokenService;
import org.cric.back_office.global.util.JwtUtil;
import org.cric.back_office.user.dto.LoginRequestDto;
import org.cric.back_office.user.dto.LoginResponseDto;
import org.cric.back_office.user.entity.User;
import org.cric.back_office.user.enums.UserRole;
import org.cric.back_office.user.enums.UserStatus;
import org.cric.back_office.user.repository.RefreshTokenRepository;
import org.cric.back_office.user.repository.UserJpaRepository;
import org.cric.back_office.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("중복 로그인 방지 테스트")
class DuplicateLoginTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    private UserService userService;

    private User testUser;
    private LoginRequestDto loginRequest;

    @BeforeEach
    void setUp() {
        userService = new UserService(
                userRepository,
                userJpaRepository,
                refreshTokenRepository,
                jwtUtil,
                passwordEncoder,
                tokenService);

        // 테스트용 사용자 설정
        testUser = mock(User.class);
        when(testUser.getId()).thenReturn(1);
        when(testUser.getEmail()).thenReturn("test@test.com");
        when(testUser.getName()).thenReturn("테스트");
        when(testUser.getPassword()).thenReturn("encodedPassword");
        when(testUser.getUserStatus()).thenReturn(UserStatus.APPROVED);
        when(testUser.getUserRole()).thenReturn(UserRole.GENERAL);

        // 로그인 요청 DTO
        loginRequest = new LoginRequestDto();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("password123");
    }

    @Test
    @DisplayName("로그인 시 새로운 tokenId가 생성되어 Redis에 저장된다")
    void login_GeneratesNewTokenIdAndSavesToRedis() {
        // given
        when(userJpaRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateTokenId()).thenReturn("token-id-123");
        when(jwtUtil.generateToken(anyInt(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn("access-token");
        when(jwtUtil.generateRefreshToken(anyInt())).thenReturn("refresh-token");
        when(jwtUtil.getAccessTokenExpiration()).thenReturn(3600000L);
        when(refreshTokenRepository.findByUserId(anyInt())).thenReturn(Optional.empty());

        // when
        userService.login(loginRequest);

        // then
        verify(jwtUtil).generateTokenId();
        verify(tokenService).saveTokenId(eq(1), eq("token-id-123"), eq(3600000L));
    }

    @Test
    @DisplayName("두 번 로그인하면 서로 다른 tokenId가 생성된다")
    void login_TwiceGeneratesDifferentTokenIds() {
        // given
        when(userJpaRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateTokenId())
                .thenReturn("first-token-id")
                .thenReturn("second-token-id");
        when(jwtUtil.generateToken(anyInt(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn("access-token");
        when(jwtUtil.generateRefreshToken(anyInt())).thenReturn("refresh-token");
        when(jwtUtil.getAccessTokenExpiration()).thenReturn(3600000L);
        when(refreshTokenRepository.findByUserId(anyInt())).thenReturn(Optional.empty());

        // when
        userService.login(loginRequest);
        userService.login(loginRequest);

        // then
        ArgumentCaptor<String> tokenIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(tokenService, times(2)).saveTokenId(eq(1), tokenIdCaptor.capture(), anyLong());

        assertThat(tokenIdCaptor.getAllValues()).containsExactly("first-token-id", "second-token-id");
        assertThat(tokenIdCaptor.getAllValues().get(0)).isNotEqualTo(tokenIdCaptor.getAllValues().get(1));
    }

    @Test
    @DisplayName("두 번째 로그인 시 Redis에 새 tokenId가 덮어쓰기 되어 첫 번째 토큰이 무효화된다")
    void login_SecondLoginOverwritesFirstTokenId() {
        // given
        when(userJpaRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateTokenId())
                .thenReturn("first-token-id")
                .thenReturn("second-token-id");
        when(jwtUtil.generateToken(anyInt(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn("access-token");
        when(jwtUtil.generateRefreshToken(anyInt())).thenReturn("refresh-token");
        when(jwtUtil.getAccessTokenExpiration()).thenReturn(3600000L);
        when(refreshTokenRepository.findByUserId(anyInt())).thenReturn(Optional.empty());

        // when
        userService.login(loginRequest); // 첫 번째 로그인
        userService.login(loginRequest); // 두 번째 로그인 (중복)

        // then
        ArgumentCaptor<String> tokenIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(tokenService, times(2)).saveTokenId(eq(1), tokenIdCaptor.capture(), anyLong());

        String lastSavedTokenId = tokenIdCaptor.getAllValues().get(1);
        assertThat(lastSavedTokenId).isEqualTo("second-token-id");
    }

    @Test
    @DisplayName("Access Token에 tokenId가 포함되어 생성된다")
    void login_AccessTokenContainsTokenId() {
        // given
        when(userJpaRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateTokenId()).thenReturn("unique-token-id");
        when(jwtUtil.generateToken(anyInt(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn("access-token");
        when(jwtUtil.generateRefreshToken(anyInt())).thenReturn("refresh-token");
        when(jwtUtil.getAccessTokenExpiration()).thenReturn(3600000L);
        when(refreshTokenRepository.findByUserId(anyInt())).thenReturn(Optional.empty());

        // when
        userService.login(loginRequest);

        // then
        verify(jwtUtil).generateToken(
                eq(1),
                eq("test@test.com"),
                eq("테스트"),
                eq("unique-token-id"), // tokenId가 포함되어 호출되는지 확인
                eq("GENERAL") // role이 포함되어 호출되는지 확인
        );
    }

    @Test
    @DisplayName("로그아웃 시 Redis에서 tokenId가 삭제된다")
    void logout_RemovesTokenIdFromRedis() {
        // given
        Integer userId = 1;

        // when
        userService.logout(userId);

        // then
        verify(tokenService).removeTokenId(userId);
        verify(refreshTokenRepository).deleteByUserId(userId);
    }

    @Test
    @DisplayName("두 번 로그인하면 서로 다른 Access Token이 반환된다")
    void login_TwiceReturnsDifferentAccessTokens() {
        // given
        when(userJpaRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateTokenId())
                .thenReturn("first-token-id")
                .thenReturn("second-token-id");
        when(jwtUtil.generateToken(anyInt(), anyString(), anyString(), eq("first-token-id"), anyString()))
                .thenReturn("access-token-1");
        when(jwtUtil.generateToken(anyInt(), anyString(), anyString(), eq("second-token-id"), anyString()))
                .thenReturn("access-token-2");
        when(jwtUtil.generateRefreshToken(anyInt())).thenReturn("refresh-token");
        when(jwtUtil.getAccessTokenExpiration()).thenReturn(3600000L);
        when(refreshTokenRepository.findByUserId(anyInt())).thenReturn(Optional.empty());

        // when
        LoginResponseDto firstLogin = userService.login(loginRequest);
        LoginResponseDto secondLogin = userService.login(loginRequest);

        // then
        assertThat(firstLogin.getAccessToken()).isEqualTo("access-token-1");
        assertThat(secondLogin.getAccessToken()).isEqualTo("access-token-2");
        assertThat(firstLogin.getAccessToken()).isNotEqualTo(secondLogin.getAccessToken());
    }
}

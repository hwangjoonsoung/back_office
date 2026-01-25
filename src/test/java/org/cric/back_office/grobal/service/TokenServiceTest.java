package org.cric.back_office.grobal.service;

import org.cric.back_office.global.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TokenService 테스트")
class TokenServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService(redisTemplate);
    }

    @Test
    @DisplayName("saveTokenId - Redis에 토큰 ID가 저장된다")
    void saveTokenId_SavesTokenIdToRedis() {
        // given
        Long userId = 1L;
        String tokenId = "test-token-id";
        long expirationMs = 3600000L;

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // when
        tokenService.saveTokenId(userId, tokenId, expirationMs);

        // then
        verify(valueOperations).set(
                eq("user:token:1"),
                eq("test-token-id"),
                eq(3600000L),
                eq(TimeUnit.MILLISECONDS)
        );
    }

    @Test
    @DisplayName("getTokenId - Redis에서 토큰 ID를 조회한다")
    void getTokenId_RetrievesTokenIdFromRedis() {
        // given
        Long userId = 1L;
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("user:token:1")).thenReturn("stored-token-id");

        // when
        String result = tokenService.getTokenId(userId);

        // then
        assertThat(result).isEqualTo("stored-token-id");
    }

    @Test
    @DisplayName("validateTokenId - 저장된 토큰 ID와 일치하면 true를 반환한다")
    void validateTokenId_ReturnsTrueWhenMatches() {
        // given
        Long userId = 1L;
        String tokenId = "valid-token-id";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("user:token:1")).thenReturn("valid-token-id");

        // when
        boolean result = tokenService.validateTokenId(userId, tokenId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("validateTokenId - 저장된 토큰 ID와 불일치하면 false를 반환한다 (중복 로그인으로 무효화된 경우)")
    void validateTokenId_ReturnsFalseWhenNotMatches() {
        // given
        Long userId = 1L;
        String oldTokenId = "old-token-id";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("user:token:1")).thenReturn("new-token-id"); // 새 로그인으로 덮어쓰여진 상태

        // when
        boolean result = tokenService.validateTokenId(userId, oldTokenId);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("validateTokenId - Redis에 토큰이 없으면 false를 반환한다 (로그아웃된 경우)")
    void validateTokenId_ReturnsFalseWhenNoTokenInRedis() {
        // given
        Long userId = 1L;
        String tokenId = "some-token-id";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("user:token:1")).thenReturn(null); // 로그아웃으로 삭제된 상태

        // when
        boolean result = tokenService.validateTokenId(userId, tokenId);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("validateTokenId - 요청 토큰 ID가 null이면 false를 반환한다")
    void validateTokenId_ReturnsFalseWhenTokenIdIsNull() {
        // given
        Long userId = 1L;

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("user:token:1")).thenReturn("stored-token-id");

        // when
        boolean result = tokenService.validateTokenId(userId, null);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("removeTokenId - Redis에서 토큰 ID가 삭제된다")
    void removeTokenId_DeletesTokenIdFromRedis() {
        // given
        Integer userId = 1;

        // when
        tokenService.removeTokenId(userId);

        // then
        verify(redisTemplate).delete("user:token:1");
    }

    @Test
    @DisplayName("중복 로그인 시나리오 - 두 번째 로그인 후 첫 번째 토큰은 검증 실패한다")
    void duplicateLoginScenario_FirstTokenBecomesInvalidAfterSecondLogin() {
        // given
        Long userId = 1L;
        String firstTokenId = "first-token-id";
        String secondTokenId = "second-token-id";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // 첫 번째 로그인: firstTokenId 저장
        tokenService.saveTokenId(userId, firstTokenId, 3600000L);

        // 두 번째 로그인: secondTokenId로 덮어쓰기
        tokenService.saveTokenId(userId, secondTokenId, 3600000L);

        // Redis에서 조회 시 secondTokenId가 반환됨 (덮어쓰기 시뮬레이션)
        when(valueOperations.get("user:token:1")).thenReturn(secondTokenId);

        // when & then
        // 첫 번째 토큰으로 검증 → 실패 (다른 기기에서 로그인하여 무효화됨)
        assertThat(tokenService.validateTokenId(userId, firstTokenId)).isFalse();

        // 두 번째 토큰으로 검증 → 성공
        assertThat(tokenService.validateTokenId(userId, secondTokenId)).isTrue();
    }
}

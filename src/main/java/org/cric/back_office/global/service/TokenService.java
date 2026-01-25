package org.cric.back_office.global.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String, String> redisTemplate;
    
    private static final String TOKEN_KEY_PREFIX = "user:token:";

    /**
     * 사용자의 토큰 ID를 Redis에 저장
     * 새로운 토큰 ID가 저장되면 기존 토큰은 자동으로 무효화됨
     */
    public void saveTokenId(Long userId, String tokenId, long expirationMs) {
        String key = TOKEN_KEY_PREFIX + userId;
        redisTemplate.opsForValue().set(key, tokenId, expirationMs, TimeUnit.MILLISECONDS);
    }

    /**
     * 사용자의 현재 유효한 토큰 ID 조회
     */
    public String getTokenId(Long userId) {
        String key = TOKEN_KEY_PREFIX + userId;
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 토큰 ID 유효성 검증
     * Redis에 저장된 토큰 ID와 일치하는지 확인
     */
    public boolean validateTokenId(Long userId, String tokenId) {
        String storedTokenId = getTokenId(userId);
        return tokenId != null && tokenId.equals(storedTokenId);
    }

    /**
     * 사용자의 토큰 ID 삭제 (로그아웃 시)
     */
    public void removeTokenId(Integer userId) {
        String key = TOKEN_KEY_PREFIX + userId;
        redisTemplate.delete(key);
    }
}

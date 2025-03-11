package com.onyxdb.idm.repositories;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

/**
 * @author ArtemFed
 */
@Repository
public class RefreshTokenRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final ValueOperations<String, String> valueOps;

    public RefreshTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
    }

    public void saveToken(String token, String userId) {
        valueOps.set(token, userId);
    }

    public String getUserIdByToken(String token) {
        return valueOps.get(token);
    }

    public void deleteToken(String token) {
        redisTemplate.delete(token);
    }
}

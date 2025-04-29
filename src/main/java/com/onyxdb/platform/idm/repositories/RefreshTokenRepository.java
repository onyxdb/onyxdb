package com.onyxdb.platform.idm.repositories;

import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.onyxdb.platform.idm.models.redis.RefreshToken;

/**
 * @author ArtemFed
 */
@Repository
public class RefreshTokenRepository {
    private final RedisTemplate<String, RefreshToken> redisTemplate;
    private final ValueOperations<String, RefreshToken> valueOps;

    public RefreshTokenRepository(RedisTemplate<String, RefreshToken> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
    }

    public void saveToken(RefreshToken token) {
        valueOps.set(token.token().toString(), token);
    }

    public Optional<RefreshToken> getByToken(String token) {
        return Optional.ofNullable(valueOps.get(token));
    }

    public void deleteToken(RefreshToken token) {
        redisTemplate.delete(token.token().toString());
    }
}

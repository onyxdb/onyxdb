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
public class RedisRefreshTokenRepository {
    private final RedisTemplate<String, RefreshToken> redisRefTokenTemplate;
    private final ValueOperations<String, RefreshToken> valueOps;
    public final String prefix = "refresh-token-";

    public RedisRefreshTokenRepository(RedisTemplate<String, RefreshToken> redisTemplate) {
        this.redisRefTokenTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
    }

    public void saveToken(RefreshToken token) {
        valueOps.set(prefix + token.token().toString(), token);
    }

    public Optional<RefreshToken> getByToken(String token) {
        return Optional.ofNullable(valueOps.get(prefix + token));
    }

    public void deleteToken(RefreshToken token) {
        redisRefTokenTemplate.delete(prefix + token.token().toString());
    }
}

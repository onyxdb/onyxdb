package com.onyxdb.platform.idm.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.onyxdb.platform.idm.models.redis.CalculatedAccessBits;

/**
 * @author ArtemFed
 */
@Repository
public class RedisAccessBitsRepository {
    private final RedisTemplate<String, CalculatedAccessBits> redisAccessBitsTemplate;
    private final ValueOperations<String, CalculatedAccessBits> valueOps;
    public final String prefix = "access-bits-";

    public RedisAccessBitsRepository(RedisTemplate<String, CalculatedAccessBits> redisAccessBitsTemplate) {
        this.redisAccessBitsTemplate = redisAccessBitsTemplate;
        this.valueOps = redisAccessBitsTemplate.opsForValue();
    }

    public void saveData(CalculatedAccessBits data) {
        valueOps.set(prefix + data.accountId().toString(), data);
    }

    public Optional<CalculatedAccessBits> getByAccount(UUID accountId) {
        return Optional.ofNullable(valueOps.get(prefix + accountId.toString()));
    }

    public void deleteData(UUID accountId) {
        redisAccessBitsTemplate.delete(prefix + accountId.toString());
    }
}

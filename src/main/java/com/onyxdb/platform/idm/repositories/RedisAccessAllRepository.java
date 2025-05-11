package com.onyxdb.platform.idm.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.onyxdb.platform.idm.models.redis.CalculatedAccessAll;

/**
 * @author ArtemFed
 */
@Repository
public class RedisAccessAllRepository {
    private final RedisTemplate<String, CalculatedAccessAll> redisAccessAllTemplate;
    private final ValueOperations<String, CalculatedAccessAll> valueOps;
    public final String prefix = "access-all-";

    public RedisAccessAllRepository(RedisTemplate<String, CalculatedAccessAll> redisAccessAllTemplate) {
        this.redisAccessAllTemplate = redisAccessAllTemplate;
        this.valueOps = redisAccessAllTemplate.opsForValue();
    }

    public void saveData(CalculatedAccessAll data) {
        valueOps.set(prefix + data.accountId().toString(), data);
    }

    public Optional<CalculatedAccessAll> getByAccount(UUID accountId) {
        return Optional.ofNullable(valueOps.get(prefix + accountId.toString()));
    }

    public void deleteData(UUID accountId) {
        redisAccessAllTemplate.delete(prefix + accountId.toString());
    }
}

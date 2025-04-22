package com.onyxdb.platform.core.clusters.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import com.onyxdb.platform.core.clusters.models.EnrichedHost;

public class EnrichedHostRedisRepository implements EnrichedHostRepository {
    private final JedisPool jedisPool;
    private final ObjectMapper objectMapper;

    public EnrichedHostRedisRepository(
            JedisPool jedisPool,
            ObjectMapper objectMapper
    ) {
        this.jedisPool = jedisPool;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<EnrichedHost> listEnrichedHosts(UUID clusterId, List<String> hosts) {
        if (hosts.isEmpty()) {
            return new ArrayList<>();
        }

        try (Jedis jedis = jedisPool.getResource()) {
            List<String> keys = hosts.stream().map(h -> buildKey(clusterId, h)).toList();
            List<String> stringEnrichedHosts = jedis.mget(keys.toArray(new String[0]))
                    .stream()
                    .filter(Objects::nonNull)
                    .toList();

            return stringEnrichedHosts.stream()
                    .map(s -> {
                        try {
                            return objectMapper.readValue(s, EnrichedHost.class);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
        }
    }

    @Override
    public void upsertEnrichedHosts(List<EnrichedHost> hosts) {
        try (Jedis jedis = jedisPool.getResource()) {
            Pipeline p = jedis.pipelined();

            hosts.forEach(h -> {
                try {
                    p.set(buildKey(h.clusterId(), h.name()), objectMapper.writeValueAsString(h));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });

            p.sync();
        }
    }

    private static String buildKey(UUID clusterId, String host) {
        return host + "." + clusterId;
    }
}

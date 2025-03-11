package com.onyxdb.idm.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.onyxdb.idm.models.redis.RefreshToken;

/**
 * @author ArtemFed
 */
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, RefreshToken> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, RefreshToken> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Настройка сериализатора для ключей
        template.setKeySerializer(new StringRedisSerializer());

        // Настройка сериализатора для значений
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // Регистрация модулей для поддержки Java 8 типов, таких как LocalDateTime
        Jackson2JsonRedisSerializer<RefreshToken> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, RefreshToken.class);

        template.setValueSerializer(serializer);

        return template;
    }
}
package com.onyxdb.platform.idm.context;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.onyxdb.platform.idm.models.redis.CalculatedAccessAll;
import com.onyxdb.platform.idm.models.redis.CalculatedAccessBits;
import com.onyxdb.platform.idm.models.redis.RefreshToken;
import com.onyxdb.platform.mdb.context.DatasourceContextConfiguration;

/**
 * @author ArtemFed
 */
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, RefreshToken> redisRefTokenTemplate(
            @Qualifier(DatasourceContextConfiguration.JEDIS_CONNECTION_FACTORY_BEAN)
            RedisConnectionFactory connectionFactory
    ) {
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

    @Bean
    public RedisTemplate<String, CalculatedAccessBits> redisAccessBitsTemplate(
            @Qualifier(DatasourceContextConfiguration.JEDIS_CONNECTION_FACTORY_BEAN)
            RedisConnectionFactory connectionFactory
    ) {
        RedisTemplate<String, CalculatedAccessBits> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Настройка сериализатора для ключей
        template.setKeySerializer(new StringRedisSerializer());

        // Настройка сериализатора для значений
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // Регистрация модулей для поддержки Java 8 типов, таких как LocalDateTime
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Jackson2JsonRedisSerializer<CalculatedAccessBits> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, CalculatedAccessBits.class);

        template.setValueSerializer(serializer);

        return template;
    }

    @Bean
    public RedisTemplate<String, CalculatedAccessAll> redisAccessAllTemplate(
            @Qualifier(DatasourceContextConfiguration.JEDIS_CONNECTION_FACTORY_BEAN)
            RedisConnectionFactory connectionFactory
    ) {
        RedisTemplate<String, CalculatedAccessAll> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Настройка сериализатора для ключей
        template.setKeySerializer(new StringRedisSerializer());

        // Настройка сериализатора для значений
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // Регистрация модулей для поддержки Java 8 типов, таких как LocalDateTime
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Jackson2JsonRedisSerializer<CalculatedAccessAll> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, CalculatedAccessAll.class);

        template.setValueSerializer(serializer);

        return template;
    }
}
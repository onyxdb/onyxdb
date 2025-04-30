package com.onyxdb.platform;

import javax.sql.DataSource;

import com.redis.testcontainers.RedisContainer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.testcontainers.clickhouse.ClickHouseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.onyxdb.platform.mdb.context.DatasourceContextConfiguration;

/**
 * @author sergey-mokhov
 */
@Configuration
public class TestContextConfiguration {
    @Bean
    public PostgreSQLContainer<?> psqlContainer() {
        PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:14.4-alpine");
        container.start();
        new HostPortWaitStrategy().waitUntilReady(container);
        return container;
    }

    @Bean
    public ClickHouseContainer clickhouseContainer() {
        ClickHouseContainer container = new ClickHouseContainer("clickhouse/clickhouse-server:24.11")
                .withDatabaseName("onyxdb");
        container.start();
        new HostPortWaitStrategy().waitUntilReady(container);
        return container;
    }

    @Bean
    @Profile("test")
    public RedisContainer redisContainer() {
        return new RedisContainer("redis:6.2.6");
    }

    @Bean(DatasourceContextConfiguration.POSTGRES_DATASOURCE_BEAN)
    @Profile("test")
    public DataSource postgresDataSource(PostgreSQLContainer<?> psqlContainer) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(psqlContainer.getJdbcUrl());
        config.setUsername(psqlContainer.getUsername());
        config.setPassword(psqlContainer.getPassword());

        return new HikariDataSource(config);
    }

    @Bean(DatasourceContextConfiguration.CLICKHOUSE_DATASOURCE_BEAN)
    @Profile("test")
    public DataSource clickhouseDataSource(ClickHouseContainer clickhouseContainer) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.clickhouse.jdbc.ClickHouseDriver");
        config.setJdbcUrl(clickhouseContainer.getJdbcUrl());
        config.setUsername(clickhouseContainer.getUsername());
        config.setPassword(clickhouseContainer.getPassword());

        return new HikariDataSource(config);
    }

    @Bean(DatasourceContextConfiguration.JEDIS_POOL_BEAN)
    @Profile("test")
    public JedisPool jedisPool(
            @Qualifier(DatasourceContextConfiguration.JEDIS_POOL_CONFIG_BEAN)
            JedisPoolConfig config,
            RedisContainer redisContainer
    ) {
        return new JedisPool(config, redisContainer.getHost(), redisContainer.getRedisPort());
    }

    @Bean(DatasourceContextConfiguration.JEDIS_CONNECTION_FACTORY_BEAN)
    @Profile("test")
    public JedisConnectionFactory jedisConnectionFactory(
            RedisContainer redisContainer
    ) {
        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
        jedisConFactory.setHostName(redisContainer.getHost());
        jedisConFactory.setPort(redisContainer.getRedisPort());

        return jedisConFactory;
    }
}

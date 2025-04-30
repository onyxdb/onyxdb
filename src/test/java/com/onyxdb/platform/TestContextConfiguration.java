package com.onyxdb.platform;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.clickhouse.ClickHouseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;

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
}

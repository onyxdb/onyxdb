package com.onyxdb.idm;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author ArtemFed
 */
@Testcontainers
@ContextConfiguration(initializers = PostgresTests.DataSourceInitializer.class)
public abstract class PostgresTests {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
            "postgres:14.4-alpine"
    )
            .withDatabaseName("ao.fedorov")
            .withUsername("ao.fedorov")
            .withPassword("")
            .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\n", 1));

    @Container
    private static final GenericContainer<?> redisContainer = new GenericContainer<>(
            "redis:latest"
    )
            .withExposedPorts(6379)
            .waitingFor(Wait.forLogMessage(".*Ready to accept connections.*\\n", 1));

    @Container
    private static final GenericContainer<?> clickHouseContainer = new GenericContainer<>(
            "yandex/clickhouse-server:latest"
    )
            .withExposedPorts(8123)
            .waitingFor(Wait.forLogMessage(".*Server started.*\\n", 1));

    static class DataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.datasource.url=" + postgresContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgresContainer.getUsername(),
                    "spring.datasource.password=" + postgresContainer.getPassword(),
                    "spring.flyway.url=" + postgresContainer.getJdbcUrl(),
                    "spring.flyway.username=" + postgresContainer.getUsername(),
                    "spring.flyway.password=" + postgresContainer.getPassword(),
                    "spring.flyway.location=" + "classpath:db/migration",
                    "spring.redis.host=" + redisContainer.getHost(),
                    "spring.redis.port=" + redisContainer.getMappedPort(6379),
                    "clickhouse.datasource.url=jdbc:clickhouse://" + clickHouseContainer.getHost() + ":" + clickHouseContainer.getMappedPort(8123) + "/default"
            );
        }
    }
}
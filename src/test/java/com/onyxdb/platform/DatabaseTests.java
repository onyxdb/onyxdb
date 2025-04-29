package com.onyxdb.platform;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.testcontainers.clickhouse.ClickHouseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author foxleren
 */
@Testcontainers
public abstract class DatabaseTests {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:14.4-alpine")
            .withDatabaseName("onyxdb")
            .withUsername("username")
            .withPassword("password")
            .withExposedPorts(5632)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(5432), new ExposedPort(5432)))
            ));

    @Container
    private static final ClickHouseContainer clickhouseContainer = new ClickHouseContainer("clickhouse/clickhouse-server:24.11")
            .withDatabaseName("onyxdb")
            .withUsername("username")
            .withPassword("password")
            .withExposedPorts(8323)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(8123), new ExposedPort(8123)))
            ));
}

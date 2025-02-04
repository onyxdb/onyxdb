package com.onyxdb.mdb;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class MdbApplicationTests {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> psqlContainer = new PostgreSQLContainer<>("postgres:14.4-alpine");

    @Test
    void contextLoads() {
    }
}

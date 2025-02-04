package com.onyxdb.mdb;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.onyxdb.mdb.repositories.ClusterPostgresRepository;

@SpringBootTest
@ActiveProfiles("test")
class MdbApplicationTests extends PostgresTests {
    @Autowired
    private ClusterPostgresRepository clusterPostgresRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testJooq() {
        clusterPostgresRepository.getByIdO(UUID.randomUUID());
    }
}

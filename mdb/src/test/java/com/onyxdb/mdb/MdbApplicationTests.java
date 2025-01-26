package com.onyxdb.mdb;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class MdbApplicationTests extends PostgresTest {
    @Test
    void contextLoads() {
    }
}

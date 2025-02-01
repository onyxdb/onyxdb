package com.onyxdb.mdb;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MdbApplicationTests extends PostgresTest {
    @Test
    void contextLoads() {
    }
}

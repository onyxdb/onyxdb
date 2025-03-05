package com.onyxdb.idm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class IdmApplicationTests extends PostgresTests {
    @Test
    void contextLoads() {
    }
}

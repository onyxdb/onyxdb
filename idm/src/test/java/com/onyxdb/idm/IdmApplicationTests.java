package com.onyxdb.idm;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Disabled
@SpringBootTest
@ActiveProfiles("test")
class IdmApplicationTests extends PostgresTests {
    @Test
    void contextLoads() {
    }
}

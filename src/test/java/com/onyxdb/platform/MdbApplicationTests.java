package com.onyxdb.platform;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MdbApplicationTests extends DatabaseTests {
    @Test
    void contextLoads() {
    }
}

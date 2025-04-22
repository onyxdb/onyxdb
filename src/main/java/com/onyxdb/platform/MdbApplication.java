package com.onyxdb.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {JooqAutoConfiguration.class})
@EnableTransactionManagement
public class MdbApplication {
    public static void main(String[] args) {
        SpringApplication.run(MdbApplication.class, args);
    }
}

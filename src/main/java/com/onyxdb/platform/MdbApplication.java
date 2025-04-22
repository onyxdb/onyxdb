package com.onyxdb.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {JooqAutoConfiguration.class})
@EnableTransactionManagement
@EnableScheduling
public class MdbApplication {
    public static void main(String[] args) {
        SpringApplication.run(MdbApplication.class, args);
    }
}

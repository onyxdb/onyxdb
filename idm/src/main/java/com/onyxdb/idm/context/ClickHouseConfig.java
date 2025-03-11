package com.onyxdb.idm.context;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.clickhouse.ClickHouseDataSource;

/**
 * @author ArtemFed
 */
@Configuration
public class ClickHouseConfig {

    @Bean
    public DataSource clickHouseDataSource() {
        return new ClickHouseDataSource("jdbc:clickhouse://localhost:8123/default");
    }

    @Bean
    public JdbcTemplate clickHouseJdbcTemplate(DataSource clickHouseDataSource) {
        return new JdbcTemplate(clickHouseDataSource);
    }
}
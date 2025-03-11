package com.onyxdb.idm.context;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
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
    public DataSource clickHouseDataSource(@Value("${clickhouse.datasource.url}") String url) {
        return new ClickHouseDataSource(url);
    }

    @Bean
    public JdbcTemplate clickHouseJdbcTemplate(DataSource clickHouseDataSource) {
        return new JdbcTemplate(clickHouseDataSource);
    }
}
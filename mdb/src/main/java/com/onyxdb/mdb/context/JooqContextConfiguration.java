package com.onyxdb.mdb.context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author foxleren
 */
@Configuration
public class JooqContextConfiguration {
    @Bean
    public DSLContext dslContext(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password) throws SQLException
    {
        Connection conn = DriverManager.getConnection(url, username, password);
        return DSL.using(conn, SQLDialect.POSTGRES);
    }
}

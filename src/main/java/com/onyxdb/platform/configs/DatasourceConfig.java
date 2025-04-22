package com.onyxdb.platform.configs;

import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.clickhouse.jdbc.ClickHouseDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jooq.SpringTransactionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author foxleren
 */
@Configuration
@EnableTransactionManagement
public class DatasourceConfig {
    public static final String CLICKHOUSE_JDBC_TEMPLATE_BEAN = "clickhouseJdbcTemplate";
    private static final String CLICKHOUSE_DATASOURCE_BEAN = "clickhouseDataSource";

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public DataSourceConnectionProvider dataSourceConnectionProvider(DataSource dataSource) {
        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
    }

    @Bean
    public SpringTransactionProvider springTransactionProvider(
            DataSourceTransactionManager dataSourceTransactionManager
    ) {
        return new SpringTransactionProvider(dataSourceTransactionManager);
    }

    @Bean
    public DSLContext dsl(
            DataSourceConnectionProvider dataSourceConnectionProvider,
            SpringTransactionProvider springTransactionProvider,
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password
    ) throws SQLException {
        org.jooq.Configuration defaultConfiguration = new DefaultConfiguration()
                .derive(DriverManager.getConnection(url, username, password))
                .derive(dataSourceConnectionProvider)
                .derive(springTransactionProvider)
                .derive(SQLDialect.POSTGRES);
        return new DefaultDSLContext(defaultConfiguration);
    }

    @Bean(CLICKHOUSE_DATASOURCE_BEAN)
    public DataSource clickhouseDataSource(
            @Value("${onyxdb.clickhouse.url}")
            String url
    ) throws SQLException {
        return new ClickHouseDataSource(url);
    }

    @Bean(CLICKHOUSE_JDBC_TEMPLATE_BEAN)
    public JdbcTemplate jdbcTemplate(
            @Qualifier(CLICKHOUSE_DATASOURCE_BEAN)
            DataSource clickhouseDataSource
    ) {
        return new JdbcTemplate(clickhouseDataSource);
    }
}

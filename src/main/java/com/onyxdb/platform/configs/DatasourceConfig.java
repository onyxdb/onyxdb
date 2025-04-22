package com.onyxdb.platform.configs;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.clickhouse.jdbc.ClickHouseDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jooq.ExceptionTranslatorExecuteListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author foxleren
 */
@Configuration
@EnableTransactionManagement
public class DatasourceConfig {
    public static final String POSTGRES_DATASOURCE_BEAN = "postgresDataSource";
    public static final String CLICKHOUSE_DATASOURCE_BEAN = "clickhouseDataSource";
    public static final String POSTGRES_TRANSACTION_TEMPLATE_BEAN = "postgresTransactionTemplate";
    public static final String CLICKHOUSE_JDBC_TEMPLATE_BEAN = "clickhouseJdbcTemplate";

    @Bean(POSTGRES_DATASOURCE_BEAN)
    public DataSource postgresDataSource(
            @Value("${onyxdb.postgres.url}")
            String url,
            @Value("${onyxdb.postgres.username}")
            String username,
            @Value("${onyxdb.postgres.password}")
            String password
    ) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl("jdbc:postgresql://localhost/onyxdb");
        config.setUsername(username);
        config.setPassword(password);

        return new HikariDataSource(config);
    }

    @Bean(POSTGRES_TRANSACTION_TEMPLATE_BEAN)
    public TransactionTemplate transactionTemplate(
            @Qualifier(POSTGRES_DATASOURCE_BEAN)
            DataSource dataSource
    ) {
        return new TransactionTemplate(new DataSourceTransactionManager(dataSource));
    }

    @Bean(CLICKHOUSE_DATASOURCE_BEAN)
    public DataSource clickhouseDataSource(
            @Value("${onyxdb.clickhouse.url}")
            String url,
            @Value("${onyxdb.clickhouse.username}")
            String username,
            @Value("${onyxdb.clickhouse.password}")
            String password
    ) throws SQLException {
        Properties properties = new Properties();
        properties.put("user", username);
        properties.put("password", password);

        return new ClickHouseDataSource(url, properties);
    }

    @Bean
    public DSLContext dsl(
            @Qualifier(POSTGRES_DATASOURCE_BEAN)
            DataSource dataSource
    ) {
        org.jooq.Configuration defaultConfiguration = new DefaultConfiguration()
                .set(new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource)))
                .set(new DefaultExecuteListenerProvider(ExceptionTranslatorExecuteListener.DEFAULT))
                .set(SQLDialect.POSTGRES);

        return new DefaultDSLContext(defaultConfiguration);
    }

    @Bean(CLICKHOUSE_JDBC_TEMPLATE_BEAN)
    public JdbcTemplate jdbcTemplate(
            @Qualifier(CLICKHOUSE_DATASOURCE_BEAN)
            DataSource clickhouseDataSource
    ) {
        return new JdbcTemplate(clickhouseDataSource);
    }
}

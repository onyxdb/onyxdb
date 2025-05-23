package com.onyxdb.platform.mdb.context;

import javax.sql.DataSource;

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
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author foxleren
 */
@Configuration
@EnableTransactionManagement
public class DatasourceContextConfiguration {
    public static final String POSTGRES_DATASOURCE_BEAN = "postgresDataSource";
    public static final String CLICKHOUSE_DATASOURCE_BEAN = "clickhouseDataSource";
    public static final String CLICKHOUSE_JDBC_TEMPLATE_BEAN = "clickhouseJdbcTemplate";
    public static final String PSQL_TRANSACTION_TEMPLATE_BEAN = "postgresTransactionTemplate";
    public static final String PSQL_JOOQ_DSL_CONTEXT = "psqlJooqDslContext";
    public static final String JEDIS_POOL_CONFIG_BEAN = "jedisPoolConfig";
    public static final String JEDIS_POOL_BEAN = "jedisConfig";
    public static final String JEDIS_CONNECTION_FACTORY_BEAN = "jedisConnectionFactory";
//    public static final String PSQL_PROPAGATION_REQUIRED_TRANSACTION_TEMPLATE_BEAN = "postgresPropagationRequiredTransactionTemplate";

    @Bean(POSTGRES_DATASOURCE_BEAN)
    @Profile("!test")
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
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);

        return new HikariDataSource(config);
    }

    @Bean(PSQL_TRANSACTION_TEMPLATE_BEAN)
    public TransactionTemplate transactionTemplate(
            @Qualifier(POSTGRES_DATASOURCE_BEAN)
            DataSource dataSource
    ) {
        var transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        return transactionTemplate;
    }

//    @Bean(PSQL_PROPAGATION_REQUIRED_TRANSACTION_TEMPLATE_BEAN)
//    public TransactionTemplate psqlPropagationRequiredTransactionTemplate(
//            @Qualifier(POSTGRES_DATASOURCE_BEAN)
//            DataSource dataSource
//    ) {
//        var transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
//        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//
//        return transactionTemplate;
//    }

    @Bean(CLICKHOUSE_DATASOURCE_BEAN)
    @Profile("!test")
    public DataSource clickhouseDataSource(
            @Value("${onyxdb.clickhouse.url}")
            String url,
            @Value("${onyxdb.clickhouse.username}")
            String username,
            @Value("${onyxdb.clickhouse.password}")
            String password
    ) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.clickhouse.jdbc.ClickHouseDriver");
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);

        return new HikariDataSource(config);
    }

    @Bean(PSQL_JOOQ_DSL_CONTEXT)
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

    @Bean(JEDIS_POOL_CONFIG_BEAN)
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setJmxEnabled(false);

        return config;
    }

    @Bean(JEDIS_POOL_BEAN)
    @Profile("!test")
    public JedisPool jedisPool(
            @Qualifier(JEDIS_POOL_CONFIG_BEAN)
            JedisPoolConfig config,
            @Value("${onyxdb.redis.host}")
            String host,
            @Value("${onyxdb.redis.port}")
            int port,
            @Value("${onyxdb.redis.user}")
            String user,
            @Value("${onyxdb.redis.password}")
            String password
    ) {
        return new JedisPool(config, host, port, user, password);
    }

    @Bean(JEDIS_CONNECTION_FACTORY_BEAN)
    @Profile("!test")
    public JedisConnectionFactory jedisConnectionFactory(
            @Value("${onyxdb.redis.host}")
            String host,
            @Value("${onyxdb.redis.port}")
            int port,
            @Value("${onyxdb.redis.user}")
            String user,
            @Value("${onyxdb.redis.password}")
            String password
    ) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        config.setUsername(user);
        config.setPassword(password);

        return new JedisConnectionFactory(config);
    }
}

package com.onyxdb.platform.configs;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {
    @Bean
    public Flyway postgresFlyway(
            @Qualifier(DatasourceConfig.POSTGRES_DATASOURCE_BEAN)
            DataSource dataSource
    ) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("db/migrations/postgres")
                .load();

        flyway.migrate();

        return flyway;
    }

    @Bean
    public Flyway clickhouseFlyway(
            @Qualifier(DatasourceConfig.CLICKHOUSE_DATASOURCE_BEAN)
            DataSource dataSource
    ) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("db/migrations/clickhouse")
                .load();

        flyway.migrate();

        return flyway;
    }
}

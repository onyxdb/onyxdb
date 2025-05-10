package com.onyxdb.platform.mdb.context;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayContextConfiguration {
    public static final String POSTGRES_FLYWAY_BEAN_NAME = "postgresFlyway";
    public static final String CLICKHOUSE_FLYWAY_BEAN_NAME = "clickhouseFlyway";

    @Bean(POSTGRES_FLYWAY_BEAN_NAME)
    public Flyway postgresFlyway(
            @Qualifier(DatasourceContextConfiguration.POSTGRES_DATASOURCE_BEAN)
            DataSource dataSource,
            @Value("${onyxdb.flyway.postgres.location}")
            String location
    ) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations(location)
                .load();

        flyway.migrate();

        return flyway;
    }

    @Bean(CLICKHOUSE_FLYWAY_BEAN_NAME)
    public Flyway clickhouseFlyway(
            @Qualifier(DatasourceContextConfiguration.CLICKHOUSE_DATASOURCE_BEAN)
            DataSource dataSource,
            @Value("${onyxdb.flyway.clickhouse.location}")
            String location
    ) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations(location)
                .load();

        flyway.migrate();

        return flyway;
    }
}

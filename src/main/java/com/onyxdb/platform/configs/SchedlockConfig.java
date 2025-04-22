package com.onyxdb.platform.configs;

import javax.sql.DataSource;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedlockConfig {
    @Bean
    public LockProvider lockProvider(
            @Qualifier(DatasourceConfig.POSTGRES_DATASOURCE_BEAN)
            DataSource dataSource
    ) {
        return new JdbcTemplateLockProvider(dataSource);
    }
}

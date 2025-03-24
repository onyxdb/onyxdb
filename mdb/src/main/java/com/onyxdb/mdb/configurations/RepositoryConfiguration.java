package com.onyxdb.mdb.configurations;

import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.mdb.hardwarePresets.HardwarePresetPostgresRepository;
import com.onyxdb.mdb.hardwarePresets.HardwarePresetRepository;
import com.onyxdb.mdb.repositories.ProjectPostgresRepository;
import com.onyxdb.mdb.repositories.ProjectRepository;

/**
 * @author foxleren
 */
@Configuration
public class RepositoryConfiguration {
    @Bean
    public HardwarePresetRepository hardwarePresetRepository(DSLContext dslContext) {
        return new HardwarePresetPostgresRepository(dslContext);
    }

    @Bean
    public ProjectRepository projectRepository(DSLContext dslContext) {
        return new ProjectPostgresRepository(dslContext);
    }
}

package com.onyxdb.mdb.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.mdb.core.clusters.ClusterHostMapper;
import com.onyxdb.mdb.core.clusters.ClusterMapper;
import com.onyxdb.mdb.core.clusters.repositories.ClusterHostPostgresRepository;
import com.onyxdb.mdb.core.clusters.repositories.ClusterHostRepository;
import com.onyxdb.mdb.core.clusters.repositories.ClusterPostgresRepository;
import com.onyxdb.mdb.core.clusters.repositories.ClusterRepository;
import com.onyxdb.mdb.core.projects.ProjectPostgresRepository;
import com.onyxdb.mdb.core.projects.ProjectRepository;
import com.onyxdb.mdb.core.resourcePresets.ResourcePresetPostgresRepository;
import com.onyxdb.mdb.core.resourcePresets.ResourcePresetRepository;
import com.onyxdb.mdb.core.zones.ZonePostgresRepository;
import com.onyxdb.mdb.core.zones.ZoneRepository;

/**
 * @author foxleren
 */
@Configuration
public class RepositoryConfig {
    @Bean
    public ResourcePresetRepository resourcePresetRepository(DSLContext dslContext) {
        return new ResourcePresetPostgresRepository(dslContext);
    }

    @Bean
    public ZoneRepository zoneRepository(DSLContext dslContext) {
        return new ZonePostgresRepository(dslContext);
    }

    @Bean
    public ProjectRepository projectRepository(DSLContext dslContext) {
        return new ProjectPostgresRepository(dslContext);
    }

    @Bean
    public ClusterRepository clusterRepository(
            DSLContext dslContext,
            ClusterMapper clusterMapper,
            ObjectMapper objectMapper
    ) {
        return new ClusterPostgresRepository(
                dslContext,
                clusterMapper,
                objectMapper
        );
    }

    @Bean
    public ClusterHostRepository clusterHostRepository(
            DSLContext dslContext,
            ClusterHostMapper clusterHostMapper
    ) {
        return new ClusterHostPostgresRepository(
                dslContext,
                clusterHostMapper
        );
    }
}

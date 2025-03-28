package com.onyxdb.mdb.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.mdb.core.clusters.ClusterMapper;
import com.onyxdb.mdb.core.clusters.ClusterService;
import com.onyxdb.mdb.core.clusters.repositories.MongoClusterRepository;
import com.onyxdb.mdb.core.projects.ProjectRepository;
import com.onyxdb.mdb.core.projects.ProjectService;
import com.onyxdb.mdb.core.resourcePresets.ResourcePresetRepository;
import com.onyxdb.mdb.core.resourcePresets.ResourcePresetService;
import com.onyxdb.mdb.core.zones.ZoneRepository;
import com.onyxdb.mdb.core.zones.ZoneService;

/**
 * @author foxleren
 */
@Configuration
public class ServiceConfiguration {
    @Bean
    public ResourcePresetService resourcePresetService(ResourcePresetRepository resourcePresetRepository) {
        return new ResourcePresetService(resourcePresetRepository);
    }

    @Bean
    public ZoneService zoneService(ZoneRepository zoneRepository) {
        return new ZoneService(zoneRepository);
    }

    @Bean
    public ProjectService projectService(ProjectRepository projectRepository) {
        return new ProjectService(projectRepository);
    }

    @Bean
    public ClusterService clusterService(
            ClusterMapper clusterMapper,
            MongoClusterRepository mongoClusterRepository,
            TransactionTemplate transactionTemplate
    ) {
        return new ClusterService(
                clusterMapper,
                mongoClusterRepository,
                transactionTemplate
        );
    }
}

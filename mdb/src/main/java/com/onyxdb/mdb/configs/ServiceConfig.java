package com.onyxdb.mdb.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.mdb.core.clusters.ClusterHostService;
import com.onyxdb.mdb.core.clusters.ClusterMapper;
import com.onyxdb.mdb.core.clusters.ClusterService;
import com.onyxdb.mdb.core.clusters.repositories.ClusterHostRepository;
import com.onyxdb.mdb.core.clusters.repositories.ClusterRepository;
import com.onyxdb.mdb.core.projects.ProjectRepository;
import com.onyxdb.mdb.core.projects.ProjectService;
import com.onyxdb.mdb.core.resourcePresets.ResourcePresetRepository;
import com.onyxdb.mdb.core.resourcePresets.ResourcePresetService;
import com.onyxdb.mdb.core.zones.ZoneRepository;
import com.onyxdb.mdb.core.zones.ZoneService;
import com.onyxdb.mdb.taskProcessing.generators.mongo.MongoCreateClusterTaskGenerator;
import com.onyxdb.mdb.taskProcessing.generators.mongo.MongoDeleteClusterTaskGenerator;
import com.onyxdb.mdb.taskProcessing.generators.mongo.MongoScaleHostsTaskGenerator;
import com.onyxdb.mdb.taskProcessing.repositories.OperationRepository;
import com.onyxdb.mdb.taskProcessing.repositories.TaskRepository;

/**
 * @author foxleren
 */
@Configuration
public class ServiceConfig {
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
            ClusterRepository clusterRepository,
            TransactionTemplate transactionTemplate,
//            CompositeTaskGenerator compositeTaskGenerator,
            OperationRepository operationRepository,
            TaskRepository taskRepository,
            MongoCreateClusterTaskGenerator mongoCreateClusterTaskGenerator,
            MongoScaleHostsTaskGenerator mongoScaleHostsTaskGenerator,
            MongoDeleteClusterTaskGenerator mongoDeleteClusterTaskGenerator
    ) {
        return new ClusterService(
                clusterMapper,
                clusterRepository,
                transactionTemplate,
//                compositeTaskGenerator,
                operationRepository,
                taskRepository,
                mongoCreateClusterTaskGenerator,
                mongoScaleHostsTaskGenerator,
                mongoDeleteClusterTaskGenerator
        );
    }

    @Bean
    public ClusterHostService clusterHostService(
            ClusterHostRepository clusterHostRepository
    ) {
        return new ClusterHostService(clusterHostRepository);
    }
}

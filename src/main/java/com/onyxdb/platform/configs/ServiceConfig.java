package com.onyxdb.platform.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.billing.BillingRepository;
import com.onyxdb.platform.billing.BillingService;
import com.onyxdb.platform.mdb.hosts.HostService;
import com.onyxdb.platform.mdb.hosts.HostMapper;
import com.onyxdb.platform.mdb.hosts.EnrichedHostRepository;
import com.onyxdb.platform.mdb.hosts.HostRepository;
import com.onyxdb.platform.core.projects.ProjectRepository;
import com.onyxdb.platform.core.projects.ProjectService;
import com.onyxdb.platform.core.resourcePresets.ResourcePresetRepository;
import com.onyxdb.platform.core.resourcePresets.ResourcePresetService;
import com.onyxdb.platform.core.zones.ZoneRepository;
import com.onyxdb.platform.core.zones.ZoneService;
import com.onyxdb.platform.operationsOLD.OperationService;
import com.onyxdb.platform.quotas.QuotaMapper;
import com.onyxdb.platform.quotas.QuotaRepository;
import com.onyxdb.platform.quotas.QuotaService;
import com.onyxdb.platform.resources.ResourceRepository;
import com.onyxdb.platform.resources.ResourceService;
import com.onyxdb.platform.processing.repositories.OperationRepository;
import com.onyxdb.platform.processing.repositories.TaskRepository;

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

//    @Bean
//    public ClusterService clusterService(
//            ClusterMapper clusterMapper,
//            ClusterRepository clusterRepository,
//            TransactionTemplate transactionTemplate,
////            CompositeTaskGenerator compositeTaskGenerator,
//            OperationRepository operationRepository,
//            TaskRepository taskRepository,
//            MongoCreateClusterTaskGenerator mongoCreateClusterTaskGenerator,
//            MongoScaleHostsTaskGenerator mongoScaleHostsTaskGenerator,
//            MongoDeleteClusterTaskGenerator mongoDeleteClusterTaskGenerator,
//            DatabaseRepository databaseRepository,
//            DatabaseMapper databaseMapper,
//            UserRepository userRepository,
//            UserMapper userMapper,
//            PsmdbClient psmdbClient,
//            MongoCreateDatabaseTaskGenerator mongoCreateDatabaseTaskGenerator,
//            MongoDeleteDatabaseTaskGenerator mongoDeleteDatabaseTaskGenerator
//    ) {
//        return new ClusterService(
//                clusterMapper,
//                clusterRepository,
//                transactionTemplate,
////                compositeTaskGenerator,
//                operationRepository,
//                taskRepository,
//                mongoCreateClusterTaskGenerator,
//                mongoScaleHostsTaskGenerator,
//                mongoDeleteClusterTaskGenerator,
//                databaseRepository,
//                databaseMapper,
//                userRepository,
//                userMapper,
//                psmdbClient,
//                mongoCreateDatabaseTaskGenerator,
//                mongoDeleteDatabaseTaskGenerator
//        );
//    }

    @Bean
    public HostService hostService(
            HostRepository hostRepository,
            EnrichedHostRepository enrichedHostRepository,
            HostMapper hostMapper
    ) {
        return new HostService(
                hostRepository,
                enrichedHostRepository,
                hostMapper
        );
    }

    @Bean
    public ResourceService resourceService(ResourceRepository resourceRepository) {
        return new ResourceService(resourceRepository);
    }

    @Bean
    public QuotaService quotaService(
            QuotaRepository quotaRepository,
            TransactionTemplate transactionTemplate,
            QuotaMapper quotaMapper,
            ResourcePresetService resourcePresetService,
            ResourceService resourceService,
            ProjectService projectService
    ) {
        return new QuotaService(
                quotaRepository,
                transactionTemplate,
                quotaMapper,
                resourcePresetService,
                resourceService,
                projectService
        );
    }

    @Bean
    public BillingService billingService(BillingRepository billingRepository) {
        return new BillingService(billingRepository);
    }

    @Bean
    public OperationService operationService(
            OperationRepository operationRepository,
            TaskRepository taskRepository,
            TransactionTemplate transactionTemplate
    ) {
        return new OperationService(
                operationRepository,
                taskRepository,
                transactionTemplate
        );
    }
}

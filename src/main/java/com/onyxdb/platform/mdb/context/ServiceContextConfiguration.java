package com.onyxdb.platform.mdb.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.mdb.billing.BillingRepository;
import com.onyxdb.platform.mdb.billing.BillingService;
import com.onyxdb.platform.mdb.hosts.EnrichedHostRepository;
import com.onyxdb.platform.mdb.hosts.HostMapper;
import com.onyxdb.platform.mdb.hosts.HostRepository;
import com.onyxdb.platform.mdb.hosts.HostService;
import com.onyxdb.platform.mdb.projects.ProjectMapper;
import com.onyxdb.platform.mdb.projects.ProjectRepository;
import com.onyxdb.platform.mdb.projects.ProjectService;
import com.onyxdb.platform.mdb.quotas.QuotaMapper;
import com.onyxdb.platform.mdb.quotas.QuotaRepository;
import com.onyxdb.platform.mdb.quotas.QuotaService;
import com.onyxdb.platform.mdb.resourcePresets.ResourcePresetRepository;
import com.onyxdb.platform.mdb.resourcePresets.ResourcePresetService;
import com.onyxdb.platform.mdb.resources.ResourceRepository;
import com.onyxdb.platform.mdb.resources.ResourceService;
import com.onyxdb.platform.mdb.zones.ZoneRepository;
import com.onyxdb.platform.mdb.zones.ZoneService;

/**
 * @author foxleren
 */
@Configuration
public class ServiceContextConfiguration {
    @Bean
    public ResourcePresetService resourcePresetService(ResourcePresetRepository resourcePresetRepository) {
        return new ResourcePresetService(resourcePresetRepository);
    }

    @Bean
    public ZoneService zoneService(ZoneRepository zoneRepository) {
        return new ZoneService(zoneRepository);
    }

    @Bean
    public ProjectService projectService(
            ProjectRepository projectRepository,
            ProjectMapper projectMapper
    ) {
        return new ProjectService(
                projectRepository,
                projectMapper
        );
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

    /// /                compositeTaskGenerator,
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
}

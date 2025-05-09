package com.onyxdb.platform.mdb.context.taskProcessing;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.mdb.clients.k8s.KubernetesAdapter;
import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbExporterServiceClient;
import com.onyxdb.platform.mdb.clients.k8s.psmdb.PsmdbExporterServiceScrapeClient;
import com.onyxdb.platform.mdb.clients.k8s.victoriaMetrics.VmServiceScrapeClient;
import com.onyxdb.platform.mdb.clients.k8s.victoriaMetrics.adapters.MongoExporterServiceScrapeAdapter;
import com.onyxdb.platform.mdb.clients.onyxdbAgent.OnyxdbAgentClient;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.databases.DatabaseRepository;
import com.onyxdb.platform.mdb.hosts.HostService;
import com.onyxdb.platform.mdb.operations.OperationService;
import com.onyxdb.platform.mdb.operations.consumers.CompositeTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.TaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.FinalTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoApplyOnyxdbAgentTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoApplyPsmdbTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoCheckOnyxdbAgentIsDeletedTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoCheckOnyxdbAgentReadinessTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoCheckPsmdbIsDeletedConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoCheckPsmdbReadinessConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoCreateBackupTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoCreateDatabaseTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoCreateExporterServiceConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoCreateExporterServiceScrapeTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoCreateUserTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoCreateVectorConfigTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoDeleteDatabaseTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoDeleteExporterServiceConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoDeleteExporterServiceScrapeTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoDeleteOnyxdbAgentTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoDeletePsmdbTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoDeleteSecretsConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoDeleteUserTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoMarkClusterDeletedTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoMarkClusterDeletingTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoMarkClusterReadyTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoMarkClusterUpdatingTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoUpdateHostsTaskConsumer;
import com.onyxdb.platform.mdb.operations.models.TaskType;
import com.onyxdb.platform.mdb.resourcePresets.ResourcePresetService;

@Configuration
public class TaskProcessorsContextConfiguration {
    @Bean
    public MongoCreateVectorConfigTaskConsumer mongoCreateVectorConfig(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient
    ) {
        return new MongoCreateVectorConfigTaskConsumer(
                objectMapper,
                clusterService,
                psmdbClient
        );
    }

    @Bean
    public MongoCheckPsmdbReadinessConsumer mongoCheckClusterReadinessProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient,
            HostService hostService
    ) {
        return new MongoCheckPsmdbReadinessConsumer(
                objectMapper,
                clusterService,
                psmdbClient,
                hostService
        );
    }

    @Bean
    public MongoCreateExporterServiceConsumer mongoCreateExporterServiceProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbExporterServiceClient psmdbExporterServiceClient
    ) {
        return new MongoCreateExporterServiceConsumer(
                objectMapper,
                clusterService,
                psmdbExporterServiceClient
        );
    }

    @Bean
    public MongoCreateExporterServiceScrapeTaskConsumer mongoCreateExporterServiceScrapeTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            VmServiceScrapeClient vmServiceScrapeClient,
            MongoExporterServiceScrapeAdapter mongoExporterServiceScrapeAdapter,
            PsmdbExporterServiceScrapeClient psmdbExporterServiceScrapeClient
    ) {
        return new MongoCreateExporterServiceScrapeTaskConsumer(
                objectMapper,
                clusterService,
                vmServiceScrapeClient,
                mongoExporterServiceScrapeAdapter,
                psmdbExporterServiceScrapeClient
        );
    }

    @Bean
    public MongoDeleteExporterServiceConsumer mongoDeleteExporterServiceProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbExporterServiceClient psmdbExporterServiceClient
    ) {
        return new MongoDeleteExporterServiceConsumer(
                objectMapper,
                clusterService,
                psmdbExporterServiceClient
        );
    }

    @Bean
    public MongoDeletePsmdbTaskConsumer mongoDeletePsmdbTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient
    ) {
        return new MongoDeletePsmdbTaskConsumer(
                objectMapper,
                clusterService,
                psmdbClient
        );
    }

    @Bean
    public MongoApplyOnyxdbAgentTaskConsumer mongoCreateOnyxdbAgentTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            KubernetesAdapter kubernetesAdapter
    ) {
        return new MongoApplyOnyxdbAgentTaskConsumer(
                objectMapper,
                clusterService,
                kubernetesAdapter
        );
    }

    @Bean
    public MongoCheckOnyxdbAgentReadinessTaskConsumer mongoCheckOnyxdbAgentReadinessTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            KubernetesAdapter kubernetesAdapter
    ) {
        return new MongoCheckOnyxdbAgentReadinessTaskConsumer(
                objectMapper,
                clusterService,
                kubernetesAdapter
        );
    }

    @Bean
    public MongoDeleteOnyxdbAgentTaskConsumer mongoDeleteOnyxdbAgentTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            KubernetesAdapter kubernetesAdapter
    ) {
        return new MongoDeleteOnyxdbAgentTaskConsumer(
                objectMapper,
                clusterService,
                kubernetesAdapter
        );
    }

    @Bean
    public MongoCheckOnyxdbAgentIsDeletedTaskConsumer mongoCheckOnyxdbAgentIsDeletedTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            KubernetesAdapter kubernetesAdapter
    ) {
        return new MongoCheckOnyxdbAgentIsDeletedTaskConsumer(
                objectMapper,
                clusterService,
                kubernetesAdapter
        );
    }

    @Bean
    public MongoCreateUserTaskConsumer mongoCreateUserTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            OnyxdbAgentClient onyxdbAgentClient,
            DatabaseRepository databaseRepository
    ) {
        return new MongoCreateUserTaskConsumer(
                objectMapper,
                clusterService,
                onyxdbAgentClient,
                databaseRepository
        );
    }

    @Bean
    public CompositeTaskConsumer compositeTaskProcessor(
            OperationService operationService,
            TransactionTemplate transactionTemplate,
            MongoApplyPsmdbTaskConsumer mongoApplyPsmdbTaskProcessor,
            MongoCheckPsmdbReadinessConsumer mongoCheckPsmdbReadinessProcessor,
            MongoApplyOnyxdbAgentTaskConsumer mongoApplyOnyxdbAgentTaskProcessor,
            MongoCheckOnyxdbAgentReadinessTaskConsumer mongoCheckOnyxdbAgentReadinessTaskProcessor,
            MongoCreateExporterServiceConsumer mongoCreateExporterServiceProcessor,
            MongoCreateExporterServiceScrapeTaskConsumer mongoCreateExporterServiceScrapeTaskProcessor,
            MongoDeleteExporterServiceScrapeTaskConsumer mongoDeleteExporterServiceScrapeTaskProcessor,
            MongoDeleteExporterServiceConsumer mongoDeleteExporterServiceProcessor,
            MongoDeletePsmdbTaskConsumer mongoDeletePsmdbTaskProcessor,
            MongoCheckPsmdbIsDeletedConsumer mongoCheckPsmdbIsDeletedProcessor,
            MongoDeleteOnyxdbAgentTaskConsumer mongoDeleteOnyxdbAgentTaskProcessor,
            MongoCheckOnyxdbAgentIsDeletedTaskConsumer mongoCheckOnyxdbAgentIsDeletedTaskProcessor,
            MongoCreateDatabaseTaskConsumer mongoCreateDatabaseTaskProcessor,
            MongoDeleteDatabaseTaskConsumer mongoDeleteDatabaseTaskProcessor,
            MongoCreateUserTaskConsumer mongoCreateUserTaskProcessor,
            FinalTaskConsumer finalTaskConsumer,
            MongoUpdateHostsTaskConsumer mongoUpdateHostsTaskProcessor,
            MongoDeleteUserTaskConsumer mongoDeleteUserTaskProcessor,
            MongoCreateBackupTaskConsumer mongoCreateBackupTaskProcessor,
            MongoDeleteSecretsConsumer mongoDeleteSecretsConsumer,
            MongoMarkClusterReadyTaskConsumer mongoMarkClusterReadyTaskProcessor,
            MongoMarkClusterUpdatingTaskConsumer mongoMarkClusterUpdatingTaskProcessor,
            MongoMarkClusterDeletingTaskConsumer mongoMarkClusterDeletingTaskProcessor,
            MongoMarkClusterDeletedTaskConsumer mongoMarkClusterDeletedTaskConsumer
    ) {
        Map<TaskType, TaskConsumer<?>> taskTypeToTaskProcessors = Map.ofEntries(
                Map.entry(
                        TaskType.MONGO_APPLY_PSMDB,
                        mongoApplyPsmdbTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_CHECK_PSMDB_READINESS,
                        mongoCheckPsmdbReadinessProcessor
                ),
                Map.entry(
                        TaskType.MONGO_APPLY_ONYXDB_AGENT,
                        mongoApplyOnyxdbAgentTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_CHECK_ONYXDB_AGENT_READINESS,
                        mongoCheckOnyxdbAgentReadinessTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_APPLY_EXPORTER_SERVICE,
                        mongoCreateExporterServiceProcessor
                ),
                Map.entry(
                        TaskType.MONGO_APPLY_EXPORTER_SERVICE_SCRAPE,
                        mongoCreateExporterServiceScrapeTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_CREATE_DATABASE,
                        mongoCreateDatabaseTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_CREATE_USER,
                        mongoCreateUserTaskProcessor
                ),
                Map.entry(
                        TaskType.FINAL_TASK,
                        finalTaskConsumer
                ),
                Map.entry(
                        TaskType.MONGO_UPDATE_HOSTS,
                        mongoUpdateHostsTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_DELETE_DATABASE,
                        mongoDeleteDatabaseTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_DELETE_USER,
                        mongoDeleteUserTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_DELETE_EXPORTER_SERVICE_SCRAPE,
                        mongoDeleteExporterServiceScrapeTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_DELETE_EXPORTER_SERVICE,
                        mongoDeleteExporterServiceProcessor
                ),
                Map.entry(
                        TaskType.MONGO_DELETE_ONYXDB_AGENT,
                        mongoDeleteOnyxdbAgentTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_CHECK_ONYXDB_AGENT_IS_DELETED,
                        mongoCheckOnyxdbAgentIsDeletedTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_DELETE_PSMDB,
                        mongoDeletePsmdbTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_CHECK_PSMDB_IS_DELETED,
                        mongoCheckPsmdbIsDeletedProcessor
                ),
                Map.entry(
                        TaskType.MONGO_CREATE_BACKUP,
                        mongoCreateBackupTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_DELETE_SECRETS,
                        mongoDeleteSecretsConsumer
                ),
                Map.entry(
                        TaskType.MONGO_MARK_CLUSTER_READY,
                        mongoMarkClusterReadyTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_MARK_CLUSTER_UPDATING,
                        mongoMarkClusterUpdatingTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_MARK_CLUSTER_DELETING,
                        mongoMarkClusterDeletingTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_MARK_CLUSTER_DELETED,
                        mongoMarkClusterDeletedTaskConsumer
                )
        );

        return new CompositeTaskConsumer(
                operationService,
                transactionTemplate,
                taskTypeToTaskProcessors
        );
    }
}

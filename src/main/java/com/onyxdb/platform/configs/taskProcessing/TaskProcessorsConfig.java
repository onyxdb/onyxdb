package com.onyxdb.platform.configs.taskProcessing;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.platform.clients.k8s.KubernetesAdapter;
import com.onyxdb.platform.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.platform.clients.k8s.psmdb.PsmdbExporterServiceClient;
import com.onyxdb.platform.clients.k8s.psmdb.PsmdbExporterServiceScrapeClient;
import com.onyxdb.platform.clients.k8s.victoriaMetrics.VmServiceScrapeClient;
import com.onyxdb.platform.clients.k8s.victoriaMetrics.adapters.MongoExporterServiceScrapeAdapter;
import com.onyxdb.platform.clients.onyxdbAgent.OnyxdbAgentClient;
import com.onyxdb.platform.core.resourcePresets.ResourcePresetService;
import com.onyxdb.platform.mdb.clusters.ClusterService;
import com.onyxdb.platform.mdb.databases.DatabaseRepository;
import com.onyxdb.platform.mdb.hosts.HostService;
import com.onyxdb.platform.processing.consumers.CompositeTaskProcessor;
import com.onyxdb.platform.processing.consumers.TaskProcessor;
import com.onyxdb.platform.processing.consumers.mongo.FinalTaskConsumer;
import com.onyxdb.platform.processing.consumers.mongo.MongoApplyOnyxdbAgentTaskProcessor;
import com.onyxdb.platform.processing.consumers.mongo.MongoApplyPsmdbTaskProcessor;
import com.onyxdb.platform.processing.consumers.mongo.MongoCheckOnyxdbAgentIsDeletedTaskProcessor;
import com.onyxdb.platform.processing.consumers.mongo.MongoCheckOnyxdbAgentReadinessTaskProcessor;
import com.onyxdb.platform.processing.consumers.mongo.MongoCheckPsmdbIsDeletedProcessor;
import com.onyxdb.platform.processing.consumers.mongo.MongoCheckPsmdbReadinessProcessor;
import com.onyxdb.platform.processing.consumers.mongo.MongoCreateBackupTaskProcessor;
import com.onyxdb.platform.processing.consumers.mongo.MongoCreateDatabaseTaskProcessor;
import com.onyxdb.platform.processing.consumers.mongo.MongoCreateExporterServiceProcessor;
import com.onyxdb.platform.processing.consumers.mongo.MongoCreateExporterServiceScrapeTaskProcessor;
import com.onyxdb.platform.processing.consumers.mongo.MongoCreateUserTaskProcessor;
import com.onyxdb.platform.processing.consumers.mongo.MongoCreateVectorConfigTaskProcessor;
import com.onyxdb.platform.processing.consumers.mongo.MongoDeleteDatabaseTaskProcessor;
import com.onyxdb.platform.processing.consumers.mongo.MongoDeleteExporterServiceProcessor;
import com.onyxdb.platform.processing.consumers.mongo.MongoDeleteExporterServiceScrapeTaskProcessor;
import com.onyxdb.platform.processing.consumers.mongo.MongoDeleteOnyxdbAgentTaskProcessor;
import com.onyxdb.platform.processing.consumers.mongo.MongoDeletePsmdbTaskProcessor;
import com.onyxdb.platform.processing.consumers.mongo.MongoDeleteUserTaskProcessor;
import com.onyxdb.platform.processing.consumers.mongo.MongoDeleteVectorConfigTaskProcessor;
import com.onyxdb.platform.processing.consumers.mongo.MongoUpdateHostsTaskProcessor;
import com.onyxdb.platform.processing.models.TaskType;
import com.onyxdb.platform.processing.repositories.TaskRepository;
import com.onyxdb.platform.services.BaseClusterService;

@Configuration
public class TaskProcessorsConfig {
    @Bean
    public MongoCreateVectorConfigTaskProcessor mongoCreateVectorConfig(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient
    ) {
        return new MongoCreateVectorConfigTaskProcessor(
                objectMapper,
                clusterService,
                psmdbClient
        );
    }

    @Bean
    public MongoApplyPsmdbTaskProcessor mongoApplyPsmdbCrTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient,
            ResourcePresetService resourcePresetService
    ) {
        return new MongoApplyPsmdbTaskProcessor(
                objectMapper,
                clusterService,
                psmdbClient,
                resourcePresetService
        );
    }

    @Bean
    public MongoCheckPsmdbReadinessProcessor mongoCheckClusterReadinessProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient,
            HostService hostService
    ) {
        return new MongoCheckPsmdbReadinessProcessor(
                objectMapper,
                clusterService,
                psmdbClient,
                hostService
        );
    }

    @Bean
    public MongoCreateExporterServiceProcessor mongoCreateExporterServiceProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbExporterServiceClient psmdbExporterServiceClient
    ) {
        return new MongoCreateExporterServiceProcessor(
                objectMapper,
                clusterService,
                psmdbExporterServiceClient
        );
    }

    @Bean
    public MongoCreateExporterServiceScrapeTaskProcessor mongoCreateExporterServiceScrapeTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            VmServiceScrapeClient vmServiceScrapeClient,
            MongoExporterServiceScrapeAdapter mongoExporterServiceScrapeAdapter,
            PsmdbExporterServiceScrapeClient psmdbExporterServiceScrapeClient
    ) {
        return new MongoCreateExporterServiceScrapeTaskProcessor(
                objectMapper,
                clusterService,
                vmServiceScrapeClient,
                mongoExporterServiceScrapeAdapter,
                psmdbExporterServiceScrapeClient
        );
    }

    @Bean
    public MongoDeleteExporterServiceScrapeTaskProcessor mongoDeleteExporterServiceScrapeTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            VmServiceScrapeClient vmServiceScrapeClient,
            MongoExporterServiceScrapeAdapter mongoExporterServiceScrapeAdapter,
            PsmdbExporterServiceScrapeClient psmdbExporterServiceScrapeClient
    ) {
        return new MongoDeleteExporterServiceScrapeTaskProcessor(
                objectMapper,
                clusterService,
                vmServiceScrapeClient,
                mongoExporterServiceScrapeAdapter,
                psmdbExporterServiceScrapeClient
        );
    }

    @Bean
    public MongoDeleteExporterServiceProcessor mongoDeleteExporterServiceProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbExporterServiceClient psmdbExporterServiceClient
    ) {
        return new MongoDeleteExporterServiceProcessor(
                objectMapper,
                clusterService,
                psmdbExporterServiceClient
        );
    }

    @Bean
    public MongoDeletePsmdbTaskProcessor mongoDeletePsmdbTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient
    ) {
        return new MongoDeletePsmdbTaskProcessor(
                objectMapper,
                clusterService,
                psmdbClient
        );
    }

//    @Bean
//    public MongoCheckPsmdbIsDeletedProcessor mongoCheckPsmdbIsDeletedProcessor(
//            ObjectMapper objectMapper,
//            ClusterService clusterService,
//            PsmdbClient psmdbClient
//    ) {
//        return new MongoCheckPsmdbIsDeletedProcessor(
//                objectMapper,
//                clusterService,
//                psmdbClient
//        );
//    }

    @Bean
    public MongoDeleteVectorConfigTaskProcessor mongoDeleteVectorConfigTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient
    ) {
        return new MongoDeleteVectorConfigTaskProcessor(
                objectMapper,
                clusterService,
                psmdbClient
        );
    }

//    @Bean
//    public MongoDeleteSecretsTaskProcessor mongoDeleteSecretsTaskProcessor(
//            ObjectMapper objectMapper,
//            ClusterService clusterService,
//            PsmdbClient psmdbClient
//    ) {
//        return new MongoDeleteSecretsTaskProcessor(
//                objectMapper,
//                clusterService,
//                psmdbClient
//        );
//    }

    @Bean
    public MongoApplyOnyxdbAgentTaskProcessor mongoCreateOnyxdbAgentTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            KubernetesAdapter kubernetesAdapter
    ) {
        return new MongoApplyOnyxdbAgentTaskProcessor(
                objectMapper,
                clusterService,
                kubernetesAdapter
        );
    }

    @Bean
    public MongoCheckOnyxdbAgentReadinessTaskProcessor mongoCheckOnyxdbAgentReadinessTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            KubernetesAdapter kubernetesAdapter
    ) {
        return new MongoCheckOnyxdbAgentReadinessTaskProcessor(
                objectMapper,
                clusterService,
                kubernetesAdapter
        );
    }

    @Bean
    public MongoDeleteOnyxdbAgentTaskProcessor mongoDeleteOnyxdbAgentTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            KubernetesAdapter kubernetesAdapter
    ) {
        return new MongoDeleteOnyxdbAgentTaskProcessor(
                objectMapper,
                clusterService,
                kubernetesAdapter
        );
    }

    @Bean
    public MongoCheckOnyxdbAgentIsDeletedTaskProcessor mongoCheckOnyxdbAgentIsDeletedTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            KubernetesAdapter kubernetesAdapter
    ) {
        return new MongoCheckOnyxdbAgentIsDeletedTaskProcessor(
                objectMapper,
                clusterService,
                kubernetesAdapter
        );
    }

//    @Bean
//    public MongoCreateDatabaseTaskProcessor mongoCreateDatabaseTaskProcessor(
//            ObjectMapper objectMapper,
//            ClusterService clusterService,
//            OnyxdbAgentClient onyxdbAgentClient
//    ) {
//        return new MongoCreateDatabaseTaskProcessor(
//                objectMapper,
//                clusterService,
//                onyxdbAgentClient
//        );
//    }

//    @Bean
//    public MongoDeleteDatabaseTaskProcessor mongoDeleteDatabaseTaskProcessor(
//            ObjectMapper objectMapper,
//            ClusterService clusterService,
//            OnyxdbAgentClient onyxdbAgentClient,
//            DatabaseRepository databaseRepository
//    ) {
//        return new MongoDeleteDatabaseTaskProcessor(
//                objectMapper,
//                clusterService,
//                onyxdbAgentClient,
//                databaseRepository
//        );
//    }

//    @Bean
//    public MongoDeleteDatabaseResultTaskProcessor mongoDeleteDatabaseResultTaskProcessor(
//            ObjectMapper objectMapper,
//            ClusterService clusterService,
//            OnyxdbAgentClient onyxdbAgentClient
//    ) {
//        return new MongoDeleteDatabaseResultTaskProcessor(
//                objectMapper,
//                clusterService,
//                onyxdbAgentClient
//        );
//    }

//    @Bean
//    public MongoCreateDatabaseResultTaskProcessor mongoCreateDatabaseResultTaskProcessor(
//            ObjectMapper objectMapper,
//            ClusterService clusterService,
//            OnyxdbAgentClient onyxdbAgentClient
//    ) {
//        return new MongoCreateDatabaseResultTaskProcessor(
//                objectMapper,
//                clusterService,
//                onyxdbAgentClient
//        );
//    }

    @Bean
    public MongoCreateUserTaskProcessor mongoCreateUserTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            OnyxdbAgentClient onyxdbAgentClient,
            DatabaseRepository databaseRepository
    ) {
        return new MongoCreateUserTaskProcessor(
                objectMapper,
                clusterService,
                onyxdbAgentClient,
                databaseRepository
        );
    }

//    @Bean
//    public MongoCreateUserResultTaskProcessor mongoCreateUserResultTaskProcessor(
//            ObjectMapper objectMapper,
//            ClusterService clusterService,
//            OnyxdbAgentClient onyxdbAgentClient
//    ) {
//        return new MongoCreateUserResultTaskProcessor(
//                objectMapper,
//                clusterService,
//                onyxdbAgentClient
//        );
//    }

    @Bean
    public CompositeTaskProcessor compositeTaskProcessor(
            BaseClusterService clusterServiceOld,
            ClusterService clusterService,
            TaskRepository taskRepository,
            MongoCreateVectorConfigTaskProcessor mongoCreateVectorConfigTaskProcessor,
            MongoApplyPsmdbTaskProcessor mongoApplyPsmdbTaskProcessor,
            MongoCheckPsmdbReadinessProcessor mongoCheckPsmdbReadinessProcessor,
            MongoApplyOnyxdbAgentTaskProcessor mongoApplyOnyxdbAgentTaskProcessor,
            MongoCheckOnyxdbAgentReadinessTaskProcessor mongoCheckOnyxdbAgentReadinessTaskProcessor,
            MongoCreateExporterServiceProcessor mongoCreateExporterServiceProcessor,
            MongoCreateExporterServiceScrapeTaskProcessor mongoCreateExporterServiceScrapeTaskProcessor,
            MongoDeleteExporterServiceScrapeTaskProcessor mongoDeleteExporterServiceScrapeTaskProcessor,
            MongoDeleteExporterServiceProcessor mongoDeleteExporterServiceProcessor,
            MongoDeletePsmdbTaskProcessor mongoDeletePsmdbTaskProcessor,
            MongoCheckPsmdbIsDeletedProcessor mongoCheckPsmdbIsDeletedProcessor,
            MongoDeleteVectorConfigTaskProcessor mongoDeleteVectorConfigTaskProcessor,
            MongoDeleteOnyxdbAgentTaskProcessor mongoDeleteOnyxdbAgentTaskProcessor,
            MongoCheckOnyxdbAgentIsDeletedTaskProcessor mongoCheckOnyxdbAgentIsDeletedTaskProcessor,
            MongoCreateDatabaseTaskProcessor mongoCreateDatabaseTaskProcessor,
            MongoDeleteDatabaseTaskProcessor mongoDeleteDatabaseTaskProcessor,
            MongoCreateUserTaskProcessor mongoCreateUserTaskProcessor,
//            MongoCreateUserResultTaskProcessor mongoCreateUserResultTaskProcessor,
            FinalTaskConsumer finalTaskConsumer,
            MongoUpdateHostsTaskProcessor mongoUpdateHostsTaskProcessor,
            MongoDeleteUserTaskProcessor mongoDeleteUserTaskProcessor,
            MongoCreateBackupTaskProcessor mongoCreateBackupTaskProcessor
//            MongoDeleteSecretsTaskProcessor mongoDeleteSecretsTaskProcessor
    ) {
        Map<TaskType, TaskProcessor<?>> taskTypeToTaskProcessors = Map.ofEntries(
                Map.entry(
                        TaskType.MONGO_APPLY_VECTOR_CONFIG,
                        mongoCreateVectorConfigTaskProcessor
                ),
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
                )
//                Map.entry(
//                        TaskType.MONGODB_DELETE_VECTOR_CONFIG,
//                        mongoDeleteVectorConfigTaskProcessor
//                ),
//                Map.entry(
//                        TaskType.MONGODB_DELETE_ONYXDB_AGENT,
//                        mongoDeleteOnyxdbAgentTaskProcessor
//                ),
//                Map.entry(
//                        TaskType.MONGODB_CHECK_ONYXDB_AGENT_IS_DELETED,
//                        mongoCheckOnyxdbAgentIsDeletedTaskProcessor
//                ),
//                Map.entry(
//                        TaskType.MONGO_CREATE_DATABASE,
//                        mongoCreateDatabaseTaskProcessor
//                ),
//                Map.entry(
//                        TaskType.MONGODB_CREATE_DATABASE_RESULT,
//                        mongoCreateDatabaseResultTaskProcessor
//                ),
//                Map.entry(
//                        TaskType.MONGODB_DELETE_DATABASE,
//                        mongoDeleteDatabaseTaskProcessor
//                ),
//                Map.entry(
//                        TaskType.MONGODB_DELETE_DATABASE_RESULT,
//                        mongoDeleteDatabaseResultTaskProcessor
//                ),
//                Map.entry(
//                        TaskType.MONGO_CREATE_USER,
//                        mongoCreateUserTaskProcessor
//                ),
//                Map.entry(
//                        TaskType.MONGODB_CREATE_USER_RESULT,
//                        mongoCreateUserResultTaskProcessor
//                )
        );

        return new CompositeTaskProcessor(
                taskTypeToTaskProcessors,
                clusterServiceOld,
                clusterService,
                taskRepository
        );
    }

//    @Bean
//    public TaskProcessingValidator taskProcessingValidator(
//            CompositeTaskGenerator compositeTaskGenerator,
//            CompositeTaskProcessor compositeTaskProcessor
//    ) {
//        return new TaskProcessingValidator(
//                compositeTaskGenerator.getTaskGenerators(),
//                compositeTaskProcessor.getTaskProcessors()
//        );
//    }
}

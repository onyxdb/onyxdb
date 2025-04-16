package com.onyxdb.mdb.configs.taskProcessing;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.mdb.clients.k8s.KubernetesAdapter;
import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbExporterServiceClient;
import com.onyxdb.mdb.clients.k8s.victoriaMetrics.VmServiceScrapeClient;
import com.onyxdb.mdb.clients.k8s.victoriaMetrics.adapters.MongoExporterServiceScrapeAdapter;
import com.onyxdb.mdb.core.clusters.ClusterService;
import com.onyxdb.mdb.core.clusters.HostService;
import com.onyxdb.mdb.core.resourcePresets.ResourcePresetService;
import com.onyxdb.mdb.services.BaseClusterService;
import com.onyxdb.mdb.taskProcessing.models.TaskType;
import com.onyxdb.mdb.taskProcessing.processors.CompositeTaskProcessor;
import com.onyxdb.mdb.taskProcessing.processors.TaskProcessor;
import com.onyxdb.mdb.taskProcessing.processors.mongo.MongoApplyPsmdbTaskProcessor;
import com.onyxdb.mdb.taskProcessing.processors.mongo.MongoCheckPsmdbIsDeletedProcessor;
import com.onyxdb.mdb.taskProcessing.processors.mongo.MongoCheckPsmdbReadinessProcessor;
import com.onyxdb.mdb.taskProcessing.processors.mongo.MongoCreateExporterServiceProcessor;
import com.onyxdb.mdb.taskProcessing.processors.mongo.MongoCreateExporterServiceScrapeTaskProcessor;
import com.onyxdb.mdb.taskProcessing.processors.mongo.MongoCreateOnyxdbAgentTaskProcessor;
import com.onyxdb.mdb.taskProcessing.processors.mongo.MongoCreateVectorConfigTaskProcessor;
import com.onyxdb.mdb.taskProcessing.processors.mongo.MongoDeleteExporterServiceProcessor;
import com.onyxdb.mdb.taskProcessing.processors.mongo.MongoDeleteExporterServiceScrapeTaskProcessor;
import com.onyxdb.mdb.taskProcessing.processors.mongo.MongoDeletePsmdbTaskProcessor;
import com.onyxdb.mdb.taskProcessing.processors.mongo.MongoDeleteVectorConfigTaskProcessor;

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
            MongoExporterServiceScrapeAdapter mongoExporterServiceScrapeAdapter
    ) {
        return new MongoCreateExporterServiceScrapeTaskProcessor(
                objectMapper,
                clusterService,
                vmServiceScrapeClient,
                mongoExporterServiceScrapeAdapter
        );
    }

    @Bean
    public MongoDeleteExporterServiceScrapeTaskProcessor mongoDeleteExporterServiceScrapeTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            VmServiceScrapeClient vmServiceScrapeClient,
            MongoExporterServiceScrapeAdapter mongoExporterServiceScrapeAdapter
    ) {
        return new MongoDeleteExporterServiceScrapeTaskProcessor(
                objectMapper,
                clusterService,
                vmServiceScrapeClient,
                mongoExporterServiceScrapeAdapter
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

    @Bean
    public MongoCheckPsmdbIsDeletedProcessor mongoCheckPsmdbIsDeletedProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient
    ) {
        return new MongoCheckPsmdbIsDeletedProcessor(
                objectMapper,
                clusterService,
                psmdbClient
        );
    }

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

    @Bean
    public MongoCreateOnyxdbAgentTaskProcessor mongoCreateOnyxdbAgentTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            KubernetesAdapter kubernetesAdapter
    ) {
        return new MongoCreateOnyxdbAgentTaskProcessor(
                objectMapper,
                clusterService,
                kubernetesAdapter
        );
    }

    @Bean
    public CompositeTaskProcessor compositeTaskProcessor(
            BaseClusterService clusterServiceOld,
            ClusterService clusterService,
            MongoCreateVectorConfigTaskProcessor mongoCreateVectorConfigTaskProcessor,
            MongoApplyPsmdbTaskProcessor mongoApplyPsmdbTaskProcessor,
            MongoCheckPsmdbReadinessProcessor mongoCheckPsmdbReadinessProcessor,
            MongoCreateOnyxdbAgentTaskProcessor mongoCreateOnyxdbAgentTaskProcessor,
            MongoCreateExporterServiceProcessor mongoCreateExporterServiceProcessor,
            MongoCreateExporterServiceScrapeTaskProcessor mongoCreateExporterServiceScrapeTaskProcessor,
            MongoDeleteExporterServiceScrapeTaskProcessor mongoDeleteExporterServiceScrapeTaskProcessor,
            MongoDeleteExporterServiceProcessor mongoDeleteExporterServiceProcessor,
            MongoDeletePsmdbTaskProcessor mongoDeletePsmdbTaskProcessor,
            MongoCheckPsmdbIsDeletedProcessor mongoCheckPsmdbIsDeletedProcessor,
            MongoDeleteVectorConfigTaskProcessor mongoDeleteVectorConfigTaskProcessor
    ) {
        Map<TaskType, TaskProcessor<?>> taskTypeToTaskProcessors = Map.ofEntries(
                Map.entry(
                        TaskType.MONGODB_CREATE_VECTOR_CONFIG,
                        mongoCreateVectorConfigTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGODB_APPLY_PSMDB,
                        mongoApplyPsmdbTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGODB_CHECK_PSMDB_READINESS,
                        mongoCheckPsmdbReadinessProcessor
                ),
                Map.entry(
                        TaskType.MONGODB_APPLY_ONYXDB_AGENT,
                        mongoCreateOnyxdbAgentTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGODB_CREATE_EXPORTER_SERVICE,
                        mongoCreateExporterServiceProcessor
                ),
                Map.entry(
                        TaskType.MONGODB_CREATE_EXPORTER_SERVICE_SCRAPE,
                        mongoCreateExporterServiceScrapeTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGODB_DELETE_EXPORTER_SERVICE_SCRAPE,
                        mongoDeleteExporterServiceScrapeTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGODB_DELETE_EXPORTER_SERVICE,
                        mongoDeleteExporterServiceProcessor
                ),
                Map.entry(
                        TaskType.MONGODB_DELETE_PSMDB,
                        mongoDeletePsmdbTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGODB_CHECK_PSMDB_IS_DELETED,
                        mongoCheckPsmdbIsDeletedProcessor
                ),
                Map.entry(
                        TaskType.MONGODB_DELETE_VECTOR_CONFIG,
                        mongoDeleteVectorConfigTaskProcessor
                )
        );

        return new CompositeTaskProcessor(
                taskTypeToTaskProcessors,
                clusterServiceOld,
                clusterService
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

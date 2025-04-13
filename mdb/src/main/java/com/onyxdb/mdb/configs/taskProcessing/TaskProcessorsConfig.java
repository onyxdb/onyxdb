package com.onyxdb.mdb.configs.taskProcessing;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbClient;
import com.onyxdb.mdb.clients.k8s.psmdb.PsmdbExporterServiceClient;
import com.onyxdb.mdb.clients.k8s.victoriaMetrics.VmServiceScrapeClient;
import com.onyxdb.mdb.clients.k8s.victoriaMetrics.adapters.MongoExporterServiceScrapeAdapter;
import com.onyxdb.mdb.core.clusters.ClusterService;
import com.onyxdb.mdb.services.BaseClusterService;
import com.onyxdb.mdb.taskProcessing.models.TaskType;
import com.onyxdb.mdb.taskProcessing.processors.CompositeTaskProcessor;
import com.onyxdb.mdb.taskProcessing.processors.TaskProcessor;
import com.onyxdb.mdb.taskProcessing.processors.mongo.MongoApplyPsmdbCrTaskProcessor;
import com.onyxdb.mdb.taskProcessing.processors.mongo.MongoCheckClusterReadinessProcessor;
import com.onyxdb.mdb.taskProcessing.processors.mongo.MongoCheckExporterServiceReadinessProcessor;
import com.onyxdb.mdb.taskProcessing.processors.mongo.MongoCheckExporterServiceScrapeReadinessTaskProcessor;
import com.onyxdb.mdb.taskProcessing.processors.mongo.MongoCreateExporterServiceProcessor;
import com.onyxdb.mdb.taskProcessing.processors.mongo.MongoCreateExporterServiceScrapeTaskProcessor;
import com.onyxdb.mdb.taskProcessing.processors.mongo.MongoCreateVectorConfigTaskProcessor;

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
    public MongoApplyPsmdbCrTaskProcessor mongoApplyPsmdbCrTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient
    ) {
        return new MongoApplyPsmdbCrTaskProcessor(
                objectMapper,
                clusterService,
                psmdbClient
        );
    }

    @Bean
    public MongoCheckClusterReadinessProcessor mongoCheckClusterReadinessProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbClient psmdbClient
    ) {
        return new MongoCheckClusterReadinessProcessor(
                objectMapper,
                clusterService,
                psmdbClient
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
    public MongoCheckExporterServiceReadinessProcessor mongoCheckExporterServiceReadinessProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            PsmdbExporterServiceClient psmdbExporterServiceClient
    ) {
        return new MongoCheckExporterServiceReadinessProcessor(
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
    public MongoCheckExporterServiceScrapeReadinessTaskProcessor mongoCheckExporterServiceScrapeReadinessTaskProcessor(
            ObjectMapper objectMapper,
            ClusterService clusterService,
            VmServiceScrapeClient vmServiceScrapeClient
    ) {
        return new MongoCheckExporterServiceScrapeReadinessTaskProcessor(
                objectMapper,
                clusterService,
                vmServiceScrapeClient
        );
    }

//    @Bean
//    public MongoDeleteExporterServiceScrapeTaskProcessor mongoDeleteExporterServiceScrapeTaskProcessor(
//            ObjectMapper objectMapper,
//            ClusterService clusterService,
//            VmServiceScrapeClient vmServiceScrapeClient,
//            MongoExporterServiceScrapeAdapter mongoExporterServiceScrapeAdapter
//    ) {
//        return new MongoDeleteExporterServiceScrapeTaskProcessor(
//                objectMapper,
//                clusterService,
//                vmServiceScrapeClient,
//                mongoExporterServiceScrapeAdapter
//        );
//    }


    @Bean
    public CompositeTaskProcessor compositeTaskProcessor(
            BaseClusterService clusterServiceOld,
            ClusterService clusterService,
            MongoCreateVectorConfigTaskProcessor mongoCreateVectorConfigTaskProcessor,
            MongoApplyPsmdbCrTaskProcessor mongoApplyPsmdbCrTaskProcessor,
            MongoCheckClusterReadinessProcessor mongoCheckClusterReadinessProcessor,
            MongoCreateExporterServiceProcessor mongoCreateExporterServiceProcessor,
            MongoCheckExporterServiceReadinessProcessor mongoCheckExporterServiceReadinessProcessor,
            MongoCreateExporterServiceScrapeTaskProcessor mongoCreateExporterServiceScrapeTaskProcessor,
            MongoCheckExporterServiceScrapeReadinessTaskProcessor mongoCheckExporterServiceScrapeReadinessTaskProcessor
    ) {
        Map<TaskType, TaskProcessor<?>> taskTypeToTaskProcessors = Map.ofEntries(
                Map.entry(
                        TaskType.MONGODB_CREATE_VECTOR_CONFIG,
                        mongoCreateVectorConfigTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGODB_APPLY_PSMDB_CR,
                        mongoApplyPsmdbCrTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGODB_CHECK_CLUSTER_READINESS,
                        mongoCheckClusterReadinessProcessor
                ),
                Map.entry(
                        TaskType.MONGODB_CREATE_EXPORTER_SERVICE,
                        mongoCreateExporterServiceProcessor
                ),
                Map.entry(
                        TaskType.MONGODB_CHECK_EXPORTER_SERVICE_READINESS,
                        mongoCheckExporterServiceReadinessProcessor
                ),
                Map.entry(
                        TaskType.MONGODB_CREATE_EXPORTER_SERVICE_SCRAPE,
                        mongoCreateExporterServiceScrapeTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGODB_CHECK_EXPORTER_SERVICE_SCRAPE_READINESS,
                        mongoCheckExporterServiceScrapeReadinessTaskProcessor
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

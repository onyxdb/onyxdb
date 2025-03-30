package com.onyxdb.mdb.configs;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import com.onyxdb.mdb.core.clusters.ClusterService;
import com.onyxdb.mdb.core.clusters.generators.ClusterTasksGenerator;
import com.onyxdb.mdb.core.clusters.generators.CompositeClusterTasksGenerator;
import com.onyxdb.mdb.core.clusters.generators.MongoClusterTasksGenerator;
import com.onyxdb.mdb.core.clusters.processors.ClusterTaskProcessor;
import com.onyxdb.mdb.core.clusters.processors.CompositeClusterTasksProcessor;
import com.onyxdb.mdb.core.clusters.processors.MongoClusterTaskProcessor;
import com.onyxdb.mdb.services.BaseClusterService;

/**
 * @author foxleren
 */
@Configuration
@EnableAsync
public class MdbContextConfig {
    @Bean
    public CompositeClusterTasksGenerator compositeClusterTasksGenerator(
            MongoClusterTasksGenerator mongoClusterTasksGenerator
    ) {
        List<ClusterTasksGenerator> generators = List.of(
                mongoClusterTasksGenerator
        );
        return new CompositeClusterTasksGenerator(generators);
    }

    @Bean
    public CompositeClusterTasksProcessor compositeClusterTasksProcessor(
            BaseClusterService clusterServiceOld,
            MongoClusterTaskProcessor mongoClusterOperationProcessor,
            ClusterService clusterService
    ) {
        List<ClusterTaskProcessor> processors = List.of(
                mongoClusterOperationProcessor
        );
        return new CompositeClusterTasksProcessor(
                processors,
                clusterServiceOld,
                clusterService
        );
    }

    @Bean(name = "processClusterTasksWorkerExecutor")
    public ExecutorService processClusterTasksWorkerExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}

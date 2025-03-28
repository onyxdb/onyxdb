package com.onyxdb.mdb.configs;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import com.onyxdb.mdb.generators.ClusterTasksGenerator;
import com.onyxdb.mdb.generators.CompositeClusterTasksGenerator;
import com.onyxdb.mdb.generators.MongoClusterTasksGenerator;
import com.onyxdb.mdb.processors.ClusterTaskProcessor;
import com.onyxdb.mdb.processors.CompositeClusterTasksProcessor;
import com.onyxdb.mdb.processors.MongoClusterTaskProcessor;
import com.onyxdb.mdb.services.BaseClusterService;

/**
 * @author foxleren
 */
@Configuration
@EnableAsync
public class MdbContextConfiguration {
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
            BaseClusterService clusterService,
            MongoClusterTaskProcessor mongoClusterOperationProcessor
    ) {
        List<ClusterTaskProcessor> processors = List.of(
                mongoClusterOperationProcessor
        );
        return new CompositeClusterTasksProcessor(processors, clusterService);
    }

    @Bean(name = "processClusterTasksWorkerExecutor")
    public ExecutorService processClusterTasksWorkerExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}

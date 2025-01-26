package com.onyxdb.mdb.context;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.mdb.generators.ClusterTasksGenerator;
import com.onyxdb.mdb.generators.CompositeClusterTasksGenerator;
import com.onyxdb.mdb.generators.MongoClusterTasksGenerator;
import com.onyxdb.mdb.processors.ClusterOperationProcessor;
import com.onyxdb.mdb.processors.CompositeClusterTasksProcessor;
import com.onyxdb.mdb.processors.MongoClusterOperationProcessor;

/**
 * @author foxleren
 */
@Configuration
public class MdbContextConfiguration {
    @Bean
    public CompositeClusterTasksGenerator compositeClusterTasksGenerator(
            MongoClusterTasksGenerator mongoClusterTasksGenerator)
    {
        List<ClusterTasksGenerator> generators = List.of(
                mongoClusterTasksGenerator
        );
        return new CompositeClusterTasksGenerator(generators);
    }

    @Bean
    public CompositeClusterTasksProcessor compositeClusterTasksProcessor(
            MongoClusterOperationProcessor mongoClusterOperationProcessor)
    {
        List<ClusterOperationProcessor> processors = List.of(
                mongoClusterOperationProcessor
        );
        return new CompositeClusterTasksProcessor(processors);
    }
}

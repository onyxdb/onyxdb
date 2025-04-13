package com.onyxdb.mdb.configs.taskProcessing;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.onyxdb.mdb.taskProcessing.generators.ClusterTaskGenerator;
import com.onyxdb.mdb.taskProcessing.generators.CompositeTaskGenerator;
import com.onyxdb.mdb.taskProcessing.generators.mongo.MongoCreateClusterTaskGenerator;
import com.onyxdb.mdb.taskProcessing.generators.mongo.MongoScaleHostsTaskGenerator;
import com.onyxdb.mdb.taskProcessing.models.OperationType;

@Configuration
public class TaskGeneratorsConfig {
    @Bean
    public MongoCreateClusterTaskGenerator mongoCreateClusterTaskGenerator(ObjectMapper objectMapper) {
        return new MongoCreateClusterTaskGenerator(objectMapper);
    }

    @Bean
    public MongoScaleHostsTaskGenerator mongoScaleHostsTaskGenerator(ObjectMapper objectMapper) {
        return new MongoScaleHostsTaskGenerator(objectMapper);
    }

    @Bean
    public CompositeTaskGenerator compositeTaskGenerator(
            MongoCreateClusterTaskGenerator mongoCreateClusterTaskGenerator,
            MongoScaleHostsTaskGenerator mongoScaleHostsTaskGenerator
    ) {
        Map<OperationType, ClusterTaskGenerator> operationTypeToClusterTaskGenerator = Map.ofEntries(
                Map.entry(
                        OperationType.MONGODB_CREATE_CLUSTER,
                        mongoCreateClusterTaskGenerator
                ),
                Map.entry(
                        OperationType.MONGODB_SCALE_HOSTS,
                        mongoScaleHostsTaskGenerator
                )
        );

        return new CompositeTaskGenerator(operationTypeToClusterTaskGenerator);
    }
}

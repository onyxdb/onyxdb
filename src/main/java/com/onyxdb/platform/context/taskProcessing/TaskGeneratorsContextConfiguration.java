package com.onyxdb.platform.context.taskProcessing;

import org.springframework.context.annotation.Configuration;
//import com.onyxdb.platform.processing.producers.mongo.MongoCreateClusterTaskGenerator;
//import com.onyxdb.platform.processing.producers.mongo.MongoCreateDatabaseTaskGenerator;
//import com.onyxdb.platform.processing.producers.mongo.MongoDeleteClusterTaskGenerator;
//import com.onyxdb.platform.processing.producers.mongo.MongoDeleteDatabaseTaskGenerator;
//import com.onyxdb.platform.processing.producers.mongo.MongoScaleHostsTaskGenerator;

@Configuration
public class TaskGeneratorsContextConfiguration {
//    @Bean
//    public MongoCreateClusterTaskGenerator mongoCreateClusterTaskGenerator(ObjectMapper objectMapper) {
//        return new MongoCreateClusterTaskGenerator(objectMapper);
//    }
//
//    @Bean
//    public MongoDeleteClusterTaskGenerator mongoDeleteClusterTaskGenerator(ObjectMapper objectMapper) {
//        return new MongoDeleteClusterTaskGenerator(objectMapper);
//    }
//
//    @Bean
//    public MongoScaleHostsTaskGenerator mongoScaleHostsTaskGenerator(ObjectMapper objectMapper) {
//        return new MongoScaleHostsTaskGenerator(objectMapper);
//    }
//
//    @Bean
//    public MongoCreateDatabaseTaskGenerator mongoCreateDatabaseTaskGenerator(ObjectMapper objectMapper) {
//        return new MongoCreateDatabaseTaskGenerator(objectMapper);
//    }
//
//    @Bean
//    public MongoDeleteDatabaseTaskGenerator mongoDeleteDatabaseTaskGenerator(ObjectMapper objectMapper) {
//        return new MongoDeleteDatabaseTaskGenerator(objectMapper);
//    }
//
//    @Bean
//    public CompositeTaskGenerator compositeTaskGenerator(
//            MongoCreateClusterTaskGenerator mongoCreateClusterTaskGenerator,
//            MongoScaleHostsTaskGenerator mongoScaleHostsTaskGenerator
//    ) {
////        Map<OperationType, ClusterTaskGenerator> operationTypeToClusterTaskGenerator = Map.ofEntries(
////                Map.entry(
////                        OperationType.MONGODB_CREATE_CLUSTER,
////                        mongoCreateClusterTaskGenerator
////                ),
////                Map.entry(
////                        OperationType.MONGODB_SCALE_HOSTS,
////                        mongoScaleHostsTaskGenerator
////                )
////        );
//
//        return new CompositeTaskGenerator();
//    }
}

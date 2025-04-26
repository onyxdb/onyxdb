//package com.onyxdb.platform.operations.tasks.producers;
//
//import java.util.List;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.stereotype.Service;
//
//import com.onyxdb.platform.core.clusters.models.ClusterToCreate;
//import com.onyxdb.platform.operations.OperationV2;
//import com.onyxdb.platform.operations.tasks.ProducedTask;
//import com.onyxdb.platform.operations.tasks.chains.CreateMongoClusterTaskChain;
//import com.onyxdb.platform.operations.tasks.producers.finalizers.FinalizeCreateMongoClusterTaskChainProducer;
//
//@Service
//public class CompositeTaskProducer {
//    private final ObjectMapper objectMapper;
//    private final TaskProducer<ClusterToCreate, CreateMongoClusterTaskChain> createMongoClusterTaskProducer;
//
//    public CompositeTaskProducer(ObjectMapper objectMapper) {
//        this.objectMapper = objectMapper;
//        this.createMongoClusterTaskProducer = buildCreateMongoClusterTaskProducer();
//    }
//
//    public List<ProducedTask> produce(OperationV2 operation) {
//        switch (operation.type()) {
//            case MONGODB_CREATE_CLUSTER:
//                return createMongoClusterTaskProducer.produce(operation, new CreateMongoClusterTaskChain());
////            case MONGODB_CREATE_USER:
////                return createMongoUserTaskProducer.produce(operation, new TaskChain());
//            default:
//                throw new UnsupportedOperationException("Unsupported operation type: " + operation.type());
//        }
//    }
//
//    private TaskProducer<ClusterToCreate, CreateMongoClusterTaskChain> buildCreateMongoClusterTaskProducer() {
//        return new CreateMongoVectorConfigTaskProducer(objectMapper)
//                .then(new CreatePsmdbTaskProducer(objectMapper))
//                .then(new CheckCreatePsmdbReadinessTaskProducer(objectMapper))
//                .then(new CreateOnyxdbAgentTaskProducer(objectMapper))
//                .then(new CheckOnyxdbAgentReadinessTaskProducer(objectMapper))
//                .then(new CreateMongoExporterServiceTaskProducer(objectMapper))
//                .then(new CreateMongoExporterServiceScrapeTaskProducer(objectMapper))
//                .then(new CreateMongoDatabaseForNewClusterTaskProducer(objectMapper))
//                .then(new CreateMongoUserForNewClusterTaskProducer(objectMapper))
//                .then(new FinalizeCreateMongoClusterTaskChainProducer());
//    }
//}

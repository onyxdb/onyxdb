//package com.onyxdb.platform.operationsOLD.tasks.producers;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import com.onyxdb.platform.core.clusters.models.ClusterToCreate;
//import com.onyxdb.platform.operationsOLD.OperationV2;
//import com.onyxdb.platform.operationsOLD.tasks.chains.CreateMongoClusterTaskChain;
//
//public abstract class CreateMongoClusterTaskProducer extends TaskProducer<ClusterToCreate, CreateMongoClusterTaskChain> {
//    protected CreateMongoClusterTaskProducer(ObjectMapper objectMapper) {
//        super(objectMapper);
//    }
//
//    protected ClusterToCreate parsePayload(OperationV2 operationPayload) {
//        try {
//            return objectMapper.readValue(operationPayload.payload(), ClusterToCreate.class);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}

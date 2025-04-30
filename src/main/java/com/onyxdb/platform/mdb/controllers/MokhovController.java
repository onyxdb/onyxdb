//package com.onyxdb.platform.controllers;
//
//import java.util.List;
//import java.util.UUID;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.onyxdb.platform.core.clusters.models.MongoPermissionToCreate;
//import com.onyxdb.platform.core.clusters.models.MongoRole;
//import com.onyxdb.platform.core.clusters.models.UserToCreate;
//import com.onyxdb.platform.operations.OperationV2;
//import com.onyxdb.platform.operations.payloads.CreateMongoUserOperationPayload;
//import com.onyxdb.platform.operations.tasks.producers.CompositeTaskProducer;
//import com.onyxdb.platform.taskProcessing.models.OperationType;
//
//@RestController
//public class MokhovController {
//    private static final Logger logger = LoggerFactory.getLogger(MokhovController.class);
//
//    private final CompositeTaskProducer compositeTaskProducer;
//    private final ObjectMapper objectMapper;
//
//    public MokhovController(CompositeTaskProducer compositeTaskProducer, ObjectMapper objectMapper) {
//        this.compositeTaskProducer = compositeTaskProducer;
//        this.objectMapper = objectMapper;
//    }
//
//    @GetMapping("/mokhov")
//    public void mokhov() throws JsonProcessingException {
//        var r = compositeTaskProducer.produce(new OperationV2(
//                UUID.randomUUID(),
//                OperationType.MONGODB_CREATE_USER,
//                objectMapper.writeValueAsString(new CreateMongoUserOperationPayload(
//                        UUID.randomUUID(),
//                        new UserToCreate(
//                                "name1",
//                                "pass1",
//                                List.of(
//                                        new MongoPermissionToCreate(
//                                                UUID.randomUUID(),
//                                                List.of(MongoRole.READ, MongoRole.READ_WRITE)
//                                        )
//                                )
//                        )
//                ))
//        ));
//        logger.error("{}", r);
//    }
//}

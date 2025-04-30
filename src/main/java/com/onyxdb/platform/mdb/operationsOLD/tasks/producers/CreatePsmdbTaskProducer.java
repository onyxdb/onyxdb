//package com.onyxdb.platform.operationsOLD.tasks.producers;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.UUID;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.stereotype.Service;
//
//import com.onyxdb.platform.operationsOLD.OperationV2;
//import com.onyxdb.platform.operationsOLD.tasks.ProducedTask;
//import com.onyxdb.platform.operationsOLD.tasks.chains.CreateMongoClusterTaskChain;
//import com.onyxdb.platform.processing.models.TaskType;
//
//@Service
//public class CreatePsmdbTaskProducer extends CreateMongoClusterTaskProducer {
//    protected CreatePsmdbTaskProducer(ObjectMapper objectMapper) {
//        super(objectMapper);
//    }
//
//    @Override
//    public List<ProducedTask> produce(OperationV2 operation, CreateMongoClusterTaskChain taskChain) {
//        var taskId = UUID.randomUUID();
//        taskChain.addTask(ProducedTask.create(
//                taskId,
//                TaskType.MONGO_APPLY_PSMDB,
//                operation.id(),
//                List.of(Objects.requireNonNull(taskChain.createMongoVectorConfigTaskId))
//        ));
//
//        taskChain.createPsmdbTaskId = taskId;
//        taskChain.addFinalTaskBlockerId(taskId);
//
//        return super.produce(operation, taskChain);
//    }
//}

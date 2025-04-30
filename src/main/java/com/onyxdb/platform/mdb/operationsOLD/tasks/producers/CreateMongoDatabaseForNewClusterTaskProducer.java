//package com.onyxdb.platform.operations.tasks.producers;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.UUID;
//
//import org.springframework.stereotype.Service;
//
//import com.onyxdb.platform.operations.OperationV2;
//import com.onyxdb.platform.operations.tasks.ProducedTask;
//import com.onyxdb.platform.operations.tasks.chains.CreateMongoClusterTaskChain;
//import com.onyxdb.platform.taskProcessing.models.TaskType;
//
//@Service
//public class CreateMongoDatabaseForNewClusterTaskProducer extends TaskProducer<CreateMongoClusterTaskChain> {
//    @Override
//    public List<ProducedTask> produce(OperationV2 operation, CreateMongoClusterTaskChain taskChain) {
//        var taskId = UUID.randomUUID();
//        taskChain.addTask(ProducedTask.create(
//                taskId,
//                TaskType.MONGO_CREATE_DATABASE,
//                operation.id(),
//                List.of(Objects.requireNonNull(taskChain.createMongoExporterServiceScrapeTaskId))
//        ));
//
//        taskChain.createMongoDatabaseTaskId = taskId;
//        taskChain.addFinalTaskBlockerId(taskId);
//
//        return super.produce(operation, taskChain);
//    }
//}

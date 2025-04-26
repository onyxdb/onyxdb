//package com.onyxdb.platform.operations.tasks.producers.finalizers;
//
//import java.util.List;
//
//import com.onyxdb.platform.operations.OperationV2;
//import com.onyxdb.platform.operations.tasks.ProducedTask;
//import com.onyxdb.platform.operations.tasks.chains.CreateMongoClusterTaskChain;
//import com.onyxdb.platform.operations.tasks.producers.TaskProducer;
//import com.onyxdb.platform.taskProcessing.models.TaskType;
//
//public class FinalizeCreateMongoClusterTaskChainProducer extends TaskProducer<CreateMongoClusterTaskChain> {
//    @Override
//    public List<ProducedTask> produce(OperationV2 operation, CreateMongoClusterTaskChain taskChain) {
//        taskChain.addTask(ProducedTask.create(
//                TaskType.FINAL_TASK,
//                operation.id(),
//                taskChain.getFinalTaskBlockerIds()
//        ));
//
//        return taskChain.getTasks();
//    }
//}

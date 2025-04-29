//package com.onyxdb.platform.operations.tasks.producers.finalizers;
//
//import java.util.List;
//
//import com.onyxdb.platform.operations.OperationV2;
//import com.onyxdb.platform.operations.tasks.ProducedTask;
//import com.onyxdb.platform.operations.tasks.chains.TaskChain;
//import com.onyxdb.platform.operations.tasks.producers.TaskProducer;
//import com.onyxdb.platform.taskProcessing.models.TaskType;
//
//public class FinalizeTaskChainProducer extends TaskProducer<TaskChain> {
//    @Override
//    public List<ProducedTask> produce(OperationV2 operation, TaskChain taskChain) {
//        taskChain.addTask(ProducedTask.create(
//                TaskType.FINAL_TASK,
//                operation.id(),
//                taskChain.getFinalTaskBlockerIds()
//        ));
//
//        return taskChain.getTasks();
//    }
//}

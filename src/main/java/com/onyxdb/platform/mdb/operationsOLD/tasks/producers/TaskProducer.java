//package com.onyxdb.platform.operationsOLD.tasks.producers;
//
//import java.util.List;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.jetbrains.annotations.Nullable;
//
//import com.onyxdb.platform.operationsOLD.OperationV2;
//import com.onyxdb.platform.operationsOLD.tasks.ProducedTask;
//import com.onyxdb.platform.operationsOLD.tasks.chains.TaskChain;
//
//public abstract class TaskProducer<OPERATION_PAYLOAD, TASK_CHAIN extends TaskChain> {
//    protected final ObjectMapper objectMapper;
//    @Nullable
//    protected TaskProducer<OPERATION_PAYLOAD, TASK_CHAIN> next;
//
//    protected TaskProducer(ObjectMapper objectMapper) {
//        this.objectMapper = objectMapper;
//    }
//
//    public List<ProducedTask> produce(OperationV2 operation, TASK_CHAIN taskChain) {
//        if (next == null) {
//            return taskChain.getTasks();
//        }
//        return next.produce(operation, taskChain);
//    }
//
//    public TaskProducer<OPERATION_PAYLOAD, TASK_CHAIN> then(TaskProducer<OPERATION_PAYLOAD, TASK_CHAIN> producer) {
//        next = producer;
//        return this;
//    }
//
//    protected abstract OPERATION_PAYLOAD parsePayload(OperationV2 operation);
//}

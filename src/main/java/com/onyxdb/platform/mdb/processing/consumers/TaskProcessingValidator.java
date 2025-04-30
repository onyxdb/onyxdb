//package com.onyxdb.mdb.taskProcessing.processors;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.context.event.EventListener;
//
//import com.onyxdb.mdb.taskProcessing.generators.TaskGenerator;
//import com.onyxdb.mdb.taskProcessing.models.TaskType;
//
//public class TaskProcessingValidator {
//    private final List<TaskGenerator<?>> generators;
//    private final List<TaskProcessor<?>> processors;
//
//    public TaskProcessingValidator(
//            List<TaskGenerator<?>> generators,
//            List<TaskProcessor<?>> processors
//    ) {
//        this.generators = generators;
//        this.processors = processors;
//
//    }
//
//    @EventListener(ContextRefreshedEvent.class)
//    public void validateProcessors() {
//        Set<TaskType> processorTaskTypes = new HashSet<>(processors.size());
//
//        for (TaskProcessor<?> processor : processors) {
//            TaskType processorTaskType = processor.getTaskType();
//
//            if (processorTaskTypes.contains(processorTaskType)) {
//                throw new RuntimeException(String.format(
//                        "Got multiple processors for task type %s", processorTaskType
//                ));
//            }
//
//            processorTaskTypes.add(processorTaskType);
//        }
//
//        Set<TaskType> generatorTaskTypes = generators.stream()
//                .map(TaskGenerator::getTaskTypeToBlockerTaskTypes)
//                .map(Map::keySet)
//                .flatMap(Collection::stream)
//                .collect(Collectors.toSet());
//
//        for (TaskType generatorTaskType : generatorTaskTypes) {
//            if (!processorTaskTypes.contains(generatorTaskType)) {
//                throw new RuntimeException(String.format(
//                        "Can't find processor for task type %s", generatorTaskType
//                ));
//            }
//        }
//    }
//}

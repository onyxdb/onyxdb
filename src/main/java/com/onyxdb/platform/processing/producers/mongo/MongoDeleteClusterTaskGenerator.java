//package com.onyxdb.platform.processing.producers.mongo;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import com.onyxdb.platform.processing.producers.ClusterTaskGenerator;
//import com.onyxdb.platform.processing.models.OperationType;
//import com.onyxdb.platform.processing.models.Task;
//import com.onyxdb.platform.processing.models.TaskType;
//import com.onyxdb.platform.processing.models.TaskWithBlockers;
//import com.onyxdb.platform.processing.models.payloads.ClusterPayload;
//
//public class MongoDeleteClusterTaskGenerator extends ClusterTaskGenerator {
//    private static final Map<TaskType, List<TaskType>> TASK_TYPE_TO_BLOCKER_TASK_TYPES = Map.ofEntries(
//            Map.entry(
//                    TaskType.MONGODB_DELETE_EXPORTER_SERVICE_SCRAPE, List.of()
//            ),
//            Map.entry(
//                    TaskType.MONGODB_DELETE_EXPORTER_SERVICE, List.of(
//                            TaskType.MONGODB_DELETE_EXPORTER_SERVICE_SCRAPE
//                    )
//            ),
//            Map.entry(
//                    TaskType.MONGODB_DELETE_ONYXDB_AGENT, List.of(
//                            TaskType.MONGODB_DELETE_EXPORTER_SERVICE_SCRAPE,
//                            TaskType.MONGODB_DELETE_EXPORTER_SERVICE
//                    )
//            ),
//            Map.entry(
//                    TaskType.MONGODB_CHECK_ONYXDB_AGENT_IS_DELETED, List.of(
//                            TaskType.MONGODB_DELETE_EXPORTER_SERVICE_SCRAPE,
//                            TaskType.MONGODB_DELETE_EXPORTER_SERVICE,
//                            TaskType.MONGODB_DELETE_ONYXDB_AGENT
//                    )
//            ),
//            Map.entry(
//                    TaskType.MONGODB_DELETE_PSMDB, List.of(
//                            TaskType.MONGODB_DELETE_EXPORTER_SERVICE_SCRAPE,
//                            TaskType.MONGODB_DELETE_EXPORTER_SERVICE,
//                            TaskType.MONGODB_DELETE_ONYXDB_AGENT,
//                            TaskType.MONGODB_CHECK_ONYXDB_AGENT_IS_DELETED
//                    )
//            ),
//            Map.entry(
//                    TaskType.MONGODB_CHECK_PSMDB_IS_DELETED, List.of(
//                            TaskType.MONGODB_DELETE_EXPORTER_SERVICE_SCRAPE,
//                            TaskType.MONGODB_DELETE_EXPORTER_SERVICE,
//                            TaskType.MONGODB_DELETE_ONYXDB_AGENT,
//                            TaskType.MONGODB_CHECK_ONYXDB_AGENT_IS_DELETED
//                    )
//            ),
//            Map.entry(
//                    TaskType.MONGODB_DELETE_VECTOR_CONFIG, List.of(
//                            TaskType.MONGODB_DELETE_EXPORTER_SERVICE_SCRAPE,
//                            TaskType.MONGODB_DELETE_EXPORTER_SERVICE,
//                            TaskType.MONGODB_CHECK_PSMDB_IS_DELETED,
//                            TaskType.MONGODB_DELETE_ONYXDB_AGENT,
//                            TaskType.MONGODB_CHECK_ONYXDB_AGENT_IS_DELETED
//                    )
//            )
//    );
//
//    public MongoDeleteClusterTaskGenerator(ObjectMapper objectMapper) {
//        super(objectMapper);
//    }
//
//    @Override
//    public OperationType getOperationType() {
//        return OperationType.MONGO_DELETE_CLUSTER;
//    }
//
//    @Override
//    public Map<TaskType, List<TaskType>> getTaskTypeToBlockerTaskTypes() {
//        return TASK_TYPE_TO_BLOCKER_TASK_TYPES;
//    }
//
//    @Override
//    public List<TaskWithBlockers> generateTasks(UUID operationId, ClusterPayload payload) {
//        LocalDateTime now = LocalDateTime.now();
//        String stringPayload = payloadToString(payload);
//
//        List<Task> tasks = List.of(
//                Task.scheduledFirst(
//                        TaskType.MONGODB_DELETE_EXPORTER_SERVICE_SCRAPE,
//                        operationId,
//                        now,
//                        DEFAULT_RETRIES_LEFT,
//                        stringPayload
//                ),
//                Task.scheduledMiddle(
//                        TaskType.MONGODB_DELETE_EXPORTER_SERVICE,
//                        operationId,
//                        now,
//                        DEFAULT_RETRIES_LEFT,
//                        stringPayload
//                ),
//                Task.scheduledMiddle(
//                        TaskType.MONGODB_DELETE_ONYXDB_AGENT,
//                        operationId,
//                        now,
//                        DEFAULT_RETRIES_LEFT,
//                        stringPayload
//                ),
//                Task.scheduledMiddle(
//                        TaskType.MONGODB_CHECK_ONYXDB_AGENT_IS_DELETED,
//                        operationId,
//                        now,
//                        DEFAULT_RETRIES_LEFT,
//                        stringPayload
//                ),
//                Task.scheduledMiddle(
//                        TaskType.MONGODB_DELETE_PSMDB,
//                        operationId,
//                        now,
//                        DEFAULT_RETRIES_LEFT,
//                        stringPayload
//                ),
//                Task.scheduledMiddle(
//                        TaskType.MONGODB_CHECK_PSMDB_IS_DELETED,
//                        operationId,
//                        now,
//                        DEFAULT_RETRIES_LEFT,
//                        stringPayload
//                ),
//                Task.scheduledLast(
//                        TaskType.MONGODB_DELETE_VECTOR_CONFIG,
//                        operationId,
//                        now,
//                        DEFAULT_RETRIES_LEFT,
//                        stringPayload
//                )
//        );
//
//        return aggregateToTasksWithBlockers(tasks);
//    }
//}

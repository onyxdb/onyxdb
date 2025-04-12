//package com.onyxdb.mdb.taskProcessing.generators.mongo;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//import com.onyxdb.mdb.core.clusters.models.ClusterType;
//import com.onyxdb.mdb.taskProcessing.generators.TaskGenerator;
//import com.onyxdb.mdb.taskProcessing.models.OperationType;
//import com.onyxdb.mdb.taskProcessing.models.Task;
//import com.onyxdb.mdb.taskProcessing.models.TaskType;
//import com.onyxdb.mdb.taskProcessing.models.TaskWithBlockers;
//
//public class MongoDeleteClusterTaskGenerator extends TaskGenerator {
//    private static final Map<TaskType, List<TaskType>> TASK_TYPE_TO_BLOCKER_TASK_TYPES = Map.ofEntries(
//            Map.entry(
//                    TaskType.MONGODB_DELETE_EXPORTER_SERVICE_SCRAPE, List.of()
//            ),
//            Map.entry(
//                    TaskType.MONGODB_CHECK_EXPORTER_SERVICE_SCRAPE_IS_DELETED, List.of(
//                            TaskType.MONGODB_DELETE_EXPORTER_SERVICE_SCRAPE
//                    )
//            ),
//            Map.entry(
//                    TaskType.MONGODB_DELETE_EXPORTER_SERVICE, List.of(
//                            TaskType.MONGODB_DELETE_EXPORTER_SERVICE_SCRAPE,
//                            TaskType.MONGODB_CHECK_EXPORTER_SERVICE_SCRAPE_IS_DELETED
//                    )
//            ),
//            Map.entry(
//                    TaskType.MONGODB_CHECK_EXPORTER_SERVICE_IS_DELETED, List.of(
//                            TaskType.MONGODB_DELETE_EXPORTER_SERVICE_SCRAPE,
//                            TaskType.MONGODB_CHECK_EXPORTER_SERVICE_SCRAPE_IS_DELETED,
//                            TaskType.MONGODB_DELETE_EXPORTER_SERVICE
//                    )
//            ),
//            Map.entry(
//                    TaskType.MONGODB_DELETE_CLUSTER, List.of(
//                            TaskType.MONGODB_DELETE_EXPORTER_SERVICE_SCRAPE,
//                            TaskType.MONGODB_CHECK_EXPORTER_SERVICE_SCRAPE_IS_DELETED,
//                            TaskType.MONGODB_DELETE_EXPORTER_SERVICE,
//                            TaskType.MONGODB_CHECK_EXPORTER_SERVICE_IS_DELETED
//                    )
//            ),
//            Map.entry(
//                    TaskType.MONGODB_CHECK_CLUSTER_IS_DELETED, List.of(
//                            TaskType.MONGODB_DELETE_EXPORTER_SERVICE_SCRAPE,
//                            TaskType.MONGODB_CHECK_EXPORTER_SERVICE_SCRAPE_IS_DELETED,
//                            TaskType.MONGODB_DELETE_EXPORTER_SERVICE,
//                            TaskType.MONGODB_CHECK_EXPORTER_SERVICE_IS_DELETED,
//                            TaskType.MONGODB_DELETE_CLUSTER
//                    )
//            )
//    );
//
//    @Override
//    public ClusterType getClusterType() {
//        return ClusterType.MONGODB;
//    }
//
//    @Override
//    public OperationType getOperationType() {
//        return OperationType.MONGODB_DELETE_CLUSTER;
//    }
//
//    @Override
//    public Map<TaskType, List<TaskType>> getTaskTypeToBlockerTaskTypes() {
//        return TASK_TYPE_TO_BLOCKER_TASK_TYPES;
//    }
//
//    public List<TaskWithBlockers> generateTasks(UUID operationId) {
//        LocalDateTime now = LocalDateTime.now();
//
//        List<Task> tasks = List.of(
//                Task.scheduledFirst(
//                        clusterId,
//                        operationId,
//                        getClusterType(),
//                        TaskType.MONGODB_DELETE_EXPORTER_SERVICE_SCRAPE,
//                        now,
//                        DEFAULT_RETRIES_LEFT
//                ),
//                Task.scheduledMiddle(
//                        clusterId,
//                        operationId,
//                        getClusterType(),
//                        TaskType.MONGODB_CHECK_EXPORTER_SERVICE_SCRAPE_IS_DELETED,
//                        now,
//                        DEFAULT_RETRIES_LEFT
//                ),
//                Task.scheduledMiddle(
//                        clusterId,
//                        operationId,
//                        getClusterType(),
//                        TaskType.MONGODB_DELETE_EXPORTER_SERVICE,
//                        now,
//                        DEFAULT_RETRIES_LEFT
//                ),
//                Task.scheduledMiddle(
//                        clusterId,
//                        operationId,
//                        getClusterType(),
//                        TaskType.MONGODB_CHECK_EXPORTER_SERVICE_IS_DELETED,
//                        now,
//                        DEFAULT_RETRIES_LEFT
//                ),
//                Task.scheduledMiddle(
//                        clusterId,
//                        operationId,
//                        getClusterType(),
//                        TaskType.MONGODB_DELETE_CLUSTER,
//                        now,
//                        DEFAULT_RETRIES_LEFT
//                ),
//                Task.scheduledLast(
//                        clusterId,
//                        operationId,
//                        getClusterType(),
//                        TaskType.MONGODB_CHECK_CLUSTER_IS_DELETED,
//                        now,
//                        DEFAULT_RETRIES_LEFT
//                )
//        );
//
//        return aggregateToTasksWithBlockers(tasks);
//    }
//}

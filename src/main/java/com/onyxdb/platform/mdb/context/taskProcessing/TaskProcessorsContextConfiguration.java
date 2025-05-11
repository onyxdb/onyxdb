package com.onyxdb.platform.mdb.context.taskProcessing;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

import com.onyxdb.platform.mdb.operations.OperationService;
import com.onyxdb.platform.mdb.operations.consumers.CompositeTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.TaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.FinalTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoApplyOnyxdbAgentServiceTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoApplyOnyxdbAgentTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoApplyPsmdbTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoCheckBackupIsDeletedTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoCheckBackupIsReadyTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoCheckClusterIsRestoredTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoCheckOnyxdbAgentIsDeletedTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoCheckOnyxdbAgentReadinessTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoCheckPsmdbIsDeletedConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoCheckPsmdbReadinessConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoCreateBackupTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoCreateDatabaseTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoCreateExporterServiceConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoCreateExporterServiceScrapeTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoCreateUserTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoDeleteBackupTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoDeleteDatabaseTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoDeleteExporterServiceConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoDeleteExporterServiceScrapeTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoDeleteOnyxdbAgentServiceTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoDeleteOnyxdbAgentTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoDeletePsmdbTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoDeleteSecretsConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoDeleteUserTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoMarkClusterDeletedTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoMarkClusterDeletingTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoMarkClusterReadyTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoMarkClusterUpdatingTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoRestoreClusterTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoUpdateHostsTaskConsumer;
import com.onyxdb.platform.mdb.operations.consumers.mongo.MongoUpdateQuotaAfterClusterDeletionTaskConsumer;
import com.onyxdb.platform.mdb.operations.models.TaskType;

@Configuration
public class TaskProcessorsContextConfiguration {
    @Bean
    public CompositeTaskConsumer compositeTaskProcessor(
            OperationService operationService,
            TransactionTemplate transactionTemplate,
            MongoApplyPsmdbTaskConsumer mongoApplyPsmdbTaskProcessor,
            MongoCheckPsmdbReadinessConsumer mongoCheckPsmdbReadinessProcessor,
            MongoApplyOnyxdbAgentTaskConsumer mongoApplyOnyxdbAgentTaskProcessor,
            MongoCheckOnyxdbAgentReadinessTaskConsumer mongoCheckOnyxdbAgentReadinessTaskProcessor,
            MongoCreateExporterServiceConsumer mongoCreateExporterServiceProcessor,
            MongoCreateExporterServiceScrapeTaskConsumer mongoCreateExporterServiceScrapeTaskProcessor,
            MongoDeleteExporterServiceScrapeTaskConsumer mongoDeleteExporterServiceScrapeTaskProcessor,
            MongoDeleteExporterServiceConsumer mongoDeleteExporterServiceProcessor,
            MongoDeletePsmdbTaskConsumer mongoDeletePsmdbTaskProcessor,
            MongoCheckPsmdbIsDeletedConsumer mongoCheckPsmdbIsDeletedProcessor,
            MongoDeleteOnyxdbAgentTaskConsumer mongoDeleteOnyxdbAgentTaskProcessor,
            MongoCheckOnyxdbAgentIsDeletedTaskConsumer mongoCheckOnyxdbAgentIsDeletedTaskProcessor,
            MongoCreateDatabaseTaskConsumer mongoCreateDatabaseTaskProcessor,
            MongoDeleteDatabaseTaskConsumer mongoDeleteDatabaseTaskProcessor,
            MongoCreateUserTaskConsumer mongoCreateUserTaskProcessor,
            FinalTaskConsumer finalTaskConsumer,
            MongoUpdateHostsTaskConsumer mongoUpdateHostsTaskProcessor,
            MongoDeleteUserTaskConsumer mongoDeleteUserTaskProcessor,
            MongoDeleteSecretsConsumer mongoDeleteSecretsConsumer,
            MongoMarkClusterReadyTaskConsumer mongoMarkClusterReadyTaskProcessor,
            MongoMarkClusterUpdatingTaskConsumer mongoMarkClusterUpdatingTaskProcessor,
            MongoMarkClusterDeletingTaskConsumer mongoMarkClusterDeletingTaskProcessor,
            MongoMarkClusterDeletedTaskConsumer mongoMarkClusterDeletedTaskConsumer,
            MongoCreateBackupTaskConsumer mongoCreateBackupTaskProcessor,
            MongoCheckBackupIsReadyTaskConsumer mongoCheckBackupIsReadyTaskConsumer,
            MongoDeleteBackupTaskConsumer mongoDeleteBackupTaskConsumer,
            MongoCheckBackupIsDeletedTaskConsumer mongoCheckBackupIsDeletedTaskConsumer,
            MongoRestoreClusterTaskConsumer mongoRestoreClusterTaskConsumer,
            MongoCheckClusterIsRestoredTaskConsumer mongoCheckClusterIsRestoredTaskConsumer,
            MongoUpdateQuotaAfterClusterDeletionTaskConsumer mongoUpdateQuotaAfterClusterDeletionTaskConsumer,
            MongoApplyOnyxdbAgentServiceTaskConsumer mongoApplyOnyxdbAgentServiceTaskConsumer,
            MongoDeleteOnyxdbAgentServiceTaskConsumer mongoDeleteOnyxdbAgentServiceTaskConsumer
    ) {
        Map<TaskType, TaskConsumer<?>> taskTypeToTaskProcessors = Map.ofEntries(
                Map.entry(
                        TaskType.MONGO_APPLY_PSMDB,
                        mongoApplyPsmdbTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_CHECK_PSMDB_READINESS,
                        mongoCheckPsmdbReadinessProcessor
                ),
                Map.entry(
                        TaskType.MONGO_APPLY_ONYXDB_AGENT,
                        mongoApplyOnyxdbAgentTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_CHECK_ONYXDB_AGENT_READINESS,
                        mongoCheckOnyxdbAgentReadinessTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_APPLY_EXPORTER_SERVICE,
                        mongoCreateExporterServiceProcessor
                ),
                Map.entry(
                        TaskType.MONGO_APPLY_EXPORTER_SERVICE_SCRAPE,
                        mongoCreateExporterServiceScrapeTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_CREATE_DATABASE,
                        mongoCreateDatabaseTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_CREATE_USER,
                        mongoCreateUserTaskProcessor
                ),
                Map.entry(
                        TaskType.FINAL_TASK,
                        finalTaskConsumer
                ),
                Map.entry(
                        TaskType.MONGO_UPDATE_HOSTS,
                        mongoUpdateHostsTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_DELETE_DATABASE,
                        mongoDeleteDatabaseTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_DELETE_USER,
                        mongoDeleteUserTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_DELETE_EXPORTER_SERVICE_SCRAPE,
                        mongoDeleteExporterServiceScrapeTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_DELETE_EXPORTER_SERVICE,
                        mongoDeleteExporterServiceProcessor
                ),
                Map.entry(
                        TaskType.MONGO_DELETE_ONYXDB_AGENT,
                        mongoDeleteOnyxdbAgentTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_CHECK_ONYXDB_AGENT_IS_DELETED,
                        mongoCheckOnyxdbAgentIsDeletedTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_DELETE_PSMDB,
                        mongoDeletePsmdbTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_CHECK_PSMDB_IS_DELETED,
                        mongoCheckPsmdbIsDeletedProcessor
                ),
                Map.entry(
                        TaskType.MONGO_UPDATE_QUOTA_AFTER_CLUSTER_DELETION,
                        mongoUpdateQuotaAfterClusterDeletionTaskConsumer
                ),
                Map.entry(
                        TaskType.MONGO_CREATE_BACKUP,
                        mongoCreateBackupTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_CHECK_BACKUP_IS_READY,
                        mongoCheckBackupIsReadyTaskConsumer
                ),
                Map.entry(
                        TaskType.MONGO_DELETE_BACKUP,
                        mongoDeleteBackupTaskConsumer
                ),
                Map.entry(
                        TaskType.MONGO_CHECK_BACKUP_IS_DELETED,
                        mongoCheckBackupIsDeletedTaskConsumer
                ),
                Map.entry(
                        TaskType.MONGO_RESTORE_CLUSTER,
                        mongoRestoreClusterTaskConsumer
                ),
                Map.entry(
                        TaskType.MONGO_CHECK_CLUSTER_IS_RESTORED,
                        mongoCheckClusterIsRestoredTaskConsumer
                ),
                Map.entry(
                        TaskType.MONGO_DELETE_SECRETS,
                        mongoDeleteSecretsConsumer
                ),
                Map.entry(
                        TaskType.MONGO_MARK_CLUSTER_READY,
                        mongoMarkClusterReadyTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_MARK_CLUSTER_UPDATING,
                        mongoMarkClusterUpdatingTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_MARK_CLUSTER_DELETING,
                        mongoMarkClusterDeletingTaskProcessor
                ),
                Map.entry(
                        TaskType.MONGO_MARK_CLUSTER_DELETED,
                        mongoMarkClusterDeletedTaskConsumer
                ),
                Map.entry(
                        TaskType.MONGO_APPLY_ONYXDB_AGENT_SERVICE,
                        mongoApplyOnyxdbAgentServiceTaskConsumer
                ),
                Map.entry(
                        TaskType.MONGO_DELETE_ONYXDB_AGENT_SERVICE,
                        mongoDeleteOnyxdbAgentServiceTaskConsumer
                )
        );

        return new CompositeTaskConsumer(
                operationService,
                transactionTemplate,
                taskTypeToTaskProcessors
        );
    }
}

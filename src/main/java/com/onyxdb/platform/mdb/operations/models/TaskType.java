package com.onyxdb.platform.mdb.operations.models;

import com.onyxdb.platform.mdb.utils.StringEnum;
import com.onyxdb.platform.mdb.utils.StringEnumResolver;

/**
 * @author foxleren
 */
public enum TaskType implements StringEnum {
    MONGO_APPLY_VECTOR_CONFIG("mongo_apply_vector_config"),
    MONGO_APPLY_PSMDB("mongo_apply_psmdb"),
    MONGO_UPDATE_HOSTS("mongo_update_hosts"),
    MONGO_CHECK_PSMDB_READINESS("mongo_check_psmdb_readiness"),
    MONGO_APPLY_ONYXDB_AGENT("mongo_apply_onyxdb_agent"),
    MONGO_APPLY_ONYXDB_AGENT_SERVICE("mongo_apply_onyxdb_agent_service"),
    MONGO_CHECK_ONYXDB_AGENT_READINESS("mongo_check_onyxdb_agent_readiness"),
    MONGO_APPLY_EXPORTER_SERVICE("mongo_apply_exporter_service"),
    MONGO_APPLY_EXPORTER_SERVICE_SCRAPE("mongo_apply_exporter_service_scrape"),
    MONGO_CREATE_DATABASE("mongo_create_database"),
    MONGO_CREATE_USER("mongo_create_user"),
    MONGO_DELETE_DATABASE("mongo_delete_database"),
    MONGO_DELETE_USER("mongo_delete_user"),


    MONGO_CREATE_BACKUP("mongo_apply_backup"),
    MONGO_CHECK_BACKUP_IS_READY("mongo_check_backup_is_ready"),
    MONGO_DELETE_BACKUP("mongo_delete_backup"),
    MONGO_CHECK_BACKUP_IS_DELETED("mongo_check_backup_is_deleted"),
    MONGO_RESTORE_CLUSTER("mongo_restore_cluster"),
    MONGO_CHECK_CLUSTER_IS_RESTORED("mongo_check_cluster_is_restored"),

    MONGO_MARK_CLUSTER_READY("mongo_mark_cluster_ready"),
    MONGO_MARK_CLUSTER_UPDATING("mongo_mark_cluster_updating"),
    MONGO_MARK_CLUSTER_DELETING("mongo_mark_cluster_deleting"),
    MONGO_MARK_CLUSTER_DELETED("mongo_mark_cluster_deleted"),

    MONGO_DELETE_EXPORTER_SERVICE_SCRAPE("mongo_delete_exporter_service_scrape"),
    MONGO_DELETE_EXPORTER_SERVICE("mongo_delete_exporter_service"),
    MONGO_DELETE_ONYXDB_AGENT_SERVICE("mongo_delete_onyxdb_agent_service"),
    MONGO_DELETE_ONYXDB_AGENT("mongo_delete_onyxdb_agent"),
    MONGO_CHECK_ONYXDB_AGENT_IS_DELETED("mongo_check_onyxdb_agent_is_deleted"),
    MONGO_DELETE_PSMDB("mongo_delete_psmdb"),
    MONGO_CHECK_PSMDB_IS_DELETED("mongo_check_psmdb_is_deleted"),
    MONGO_UPDATE_QUOTA_AFTER_CLUSTER_DELETION("mongo_update_quota_after_cluster_deletion"),
    MONGO_DELETE_SECRETS("mongo_delete_secrets"),

    // TODO delete
    MONGO_DELETE_VECTOR_CONFIG("mongo_delete_vector_config"),

    FINAL_TASK("final_task");;

    public static final StringEnumResolver<TaskType> R = new StringEnumResolver<>(TaskType.class);

    private final String value;

    TaskType(final String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}

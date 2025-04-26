package com.onyxdb.platform.processing.models;

import com.onyxdb.platform.utils.StringEnum;
import com.onyxdb.platform.utils.StringEnumResolver;

/**
 * @author foxleren
 */
public enum TaskType implements StringEnum {
    MONGO_APPLY_VECTOR_CONFIG("mongo_apply_vector_config"),
    MONGO_APPLY_PSMDB("mongo_apply_psmdb"),
    MONGO_UPDATE_HOSTS("mongo_update_hosts"),
    MONGO_CHECK_PSMDB_READINESS("mongo_check_psmdb_readiness"),
    MONGO_APPLY_ONYXDB_AGENT("mongo_apply_onyxdb_agent"),
    MONGO_CHECK_ONYXDB_AGENT_READINESS("mongo_check_onyxdb_agent_readiness"),
    MONGO_APPLY_EXPORTER_SERVICE("mongo_apply_exporter_service"),
    MONGO_APPLY_EXPORTER_SERVICE_SCRAPE("mongo_apply_exporter_service_scrape"),
    MONGO_CREATE_DATABASE("mongo_create_database"),
    MONGO_CREATE_USER("mongo_create_user"),
    MONGO_DELETE_DATABASE("mongo_delete_database"),
    MONGO_DELETE_USER("mongo_delete_user"),

    MONGODB_DELETE_EXPORTER_SERVICE_SCRAPE("mongodb_delete_exporter_service_scrape"),
    MONGODB_DELETE_EXPORTER_SERVICE("mongodb_delete_exporter_service"),
    MONGODB_DELETE_ONYXDB_AGENT("mongodb_delete_onyxdb_agent"),
    MONGODB_CHECK_ONYXDB_AGENT_IS_DELETED("mongodb_check_onyxdb_agent_is_deleted"),
    MONGODB_DELETE_PSMDB("mongodb_delete_psmdb"),
    MONGODB_CHECK_PSMDB_IS_DELETED("mongodb_check_psmdb_is_deleted"),
    MONGODB_DELETE_VECTOR_CONFIG("mongodb_delete_vector_config"),

    FINAL_TASK("final_task");
    ;

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

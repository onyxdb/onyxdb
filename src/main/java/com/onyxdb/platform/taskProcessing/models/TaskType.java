package com.onyxdb.platform.taskProcessing.models;

import com.onyxdb.platform.utils.StringEnum;

/**
 * @author foxleren
 */
public enum TaskType implements StringEnum {
    MONGODB_CREATE_VECTOR_CONFIG("mongodb_create_vector_config"),
    MONGODB_APPLY_PSMDB("mongodb_apply_psmdb"),
    MONGODB_CHECK_PSMDB_READINESS("mongodb_check_psmdb_readiness"),
    MONGODB_APPLY_ONYXDB_AGENT("mongodb_apply_onyxdb_agent"),
    MONGODB_CHECK_ONYXDB_AGENT_READINESS("mongodb_check_onyxdb_agent_readiness"),
    MONGODB_CREATE_EXPORTER_SERVICE("mongodb_create_exporter_service"),
    MONGODB_CREATE_EXPORTER_SERVICE_SCRAPE("mongodb_create_exporter_service_scrape"),

    MONGODB_DELETE_EXPORTER_SERVICE_SCRAPE("mongodb_delete_exporter_service_scrape"),
    MONGODB_DELETE_EXPORTER_SERVICE("mongodb_delete_exporter_service"),
    MONGODB_DELETE_ONYXDB_AGENT("mongodb_delete_onyxdb_agent"),
    MONGODB_CHECK_ONYXDB_AGENT_IS_DELETED("mongodb_check_onyxdb_agent_is_deleted"),
    MONGODB_DELETE_PSMDB("mongodb_delete_psmdb"),
    MONGODB_CHECK_PSMDB_IS_DELETED("mongodb_check_psmdb_is_deleted"),
    MONGODB_DELETE_VECTOR_CONFIG("mongodb_delete_vector_config"),
    ;

    private final String value;

    TaskType(final String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

    public static TaskType fromValue(String value) {
        return StringEnum.fromValue(TaskType.class, value);
    }
}

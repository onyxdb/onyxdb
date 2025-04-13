package com.onyxdb.mdb.taskProcessing.models;

import com.onyxdb.mdb.utils.StringEnum;

/**
 * @author foxleren
 */
public enum TaskType implements StringEnum {
    // mongodb:create_cluster
    MONGODB_CREATE_VECTOR_CONFIG("mongodb_create_vector_config"),
    MONGODB_CREATE_CLUSTER("mongodb_create_cluster"),
    MONGODB_CHECK_CLUSTER_READINESS("mongodb_check_cluster_readiness"),
    MONGODB_CREATE_EXPORTER_SERVICE("mongodb_create_exporter_service"),
    MONGODB_CHECK_EXPORTER_SERVICE_READINESS("mongodb_check_exporter_service_readiness"),
    MONGODB_CREATE_EXPORTER_SERVICE_SCRAPE("mongodb_create_exporter_service_scrape"),
    MONGODB_CHECK_EXPORTER_SERVICE_SCRAPE_READINESS("mongodb_check_exporter_service_scrape_readiness"),

    // mongodb:delete_cluster
    MONGODB_DELETE_EXPORTER_SERVICE_SCRAPE("mongodb_delete_exporter_service_scrape"),
    MONGODB_CHECK_EXPORTER_SERVICE_SCRAPE_IS_DELETED("mongodb_check_exporter_service_scrape_is_deleted"),
    MONGODB_DELETE_EXPORTER_SERVICE("mongodb_delete_exporter_service"),
    MONGODB_CHECK_EXPORTER_SERVICE_IS_DELETED("mongodb_check_exporter_service_is_deleted"),
    MONGODB_DELETE_CLUSTER("mongodb_delete_cluster"),
    MONGODB_CHECK_CLUSTER_IS_DELETED("mongodb_check_cluster_is_deleted"),
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

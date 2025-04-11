package com.onyxdb.mdb.core.clusters.models;

import com.onyxdb.mdb.utils.StringEnum;

/**
 * @author foxleren
 */
public enum ClusterTaskType implements StringEnum {
    MONGODB_CREATE_CLUSTER("mongodb_create_cluster"),
    MONGODB_CHECK_CLUSTER_READINESS("mongodb_check_cluster_readiness"),
    MONGODB_CREATE_EXPORTER_SERVICE("mongodb_create_exporter_service"),
    MONGODB_CHECK_EXPORTER_SERVICE_READINESS("mongodb_check_exporter_service_readiness"),
    MONGODB_CREATE_EXPORTER_SERVICE_SCRAPE("mongodb_create_exporter_service_scrape"),
    MONGODB_CHECK_EXPORTER_SERVICE_SCRAPE_READINESS("mongodb_check_exporter_service_scrape_readiness"),
    ;

    private final String value;

    ClusterTaskType(final String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

    public static ClusterTaskType fromValue(String value) {
        return StringEnum.fromValue(ClusterTaskType.class, value);
    }
}

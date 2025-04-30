package com.onyxdb.platform.mdb.models;

import java.util.UUID;

import com.onyxdb.platform.mdb.operationsOLD.payloads.OperationPayload;

/**
 * @author foxleren
 */
public record ClusterToCreate(
        String name,
        String description,
        UUID projectId,
        String namespace,
        ClusterType type,
        ClusterConfig config,
        String databaseName,
        String user,
        String password
) implements OperationPayload {
}

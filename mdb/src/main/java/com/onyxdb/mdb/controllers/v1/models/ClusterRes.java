package com.onyxdb.mdb.controllers.v1.models;

import java.util.UUID;

/**
 * @author foxleren
 */
public record ClusterRes(
        UUID id,
        String name,
        UUID projectId,
        String dbType
) {
}

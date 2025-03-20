package com.onyxdb.mdb.controllers.v1.models;

import java.util.UUID;

/**
 * @author foxleren
 */
public record OperationRes(
        UUID id,
        String name,
        String status
) {
}

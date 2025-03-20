package com.onyxdb.mdb.controllers.v1.models;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author foxleren
 */
public record ProjectRes(
        UUID id,
        UUID productId,
        String name,
        UUID createdBy,
        LocalDateTime createdAt
) {
}

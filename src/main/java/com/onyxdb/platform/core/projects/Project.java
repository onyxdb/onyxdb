package com.onyxdb.platform.core.projects;

import java.util.UUID;

/**
 * @author foxleren
 */
public record Project(
        UUID id,
        String name,
        String description,
        UUID productId,
        boolean isArchived
) {
}

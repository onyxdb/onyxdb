package com.onyxdb.mdb.core.projects;

import java.util.UUID;

/**
 * @author foxleren
 */
public record Project(
        UUID id,
        String name,
        String description,
        boolean isArchived
) {
}

package com.onyxdb.platform.mdb.projects;

import java.util.UUID;

/**
 * @author foxleren
 */
public record CreateProject(
        String name,
        String description,
        UUID productId,
        UUID createdBy
) {
}

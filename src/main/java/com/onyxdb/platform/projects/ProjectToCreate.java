package com.onyxdb.platform.projects;

import java.util.UUID;

/**
 * @author foxleren
 */
public record ProjectToCreate(
        UUID id,
        String name,
        String description,
        UUID productId
) {
}

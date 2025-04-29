package com.onyxdb.platform.projects;

import java.util.UUID;

/**
 * @author foxleren
 */
public record UpdateProject(
        UUID id,
        String name,
        String description
) {
}

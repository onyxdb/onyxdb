package com.onyxdb.platform.mdb.projects;

import java.util.UUID;

/**
 * @author foxleren
 */
public record UpdateProject(
        UUID id,
        String description
) {
}

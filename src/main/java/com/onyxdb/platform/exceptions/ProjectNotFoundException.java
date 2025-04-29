package com.onyxdb.platform.exceptions;

import java.util.UUID;

public class ProjectNotFoundException extends NotFoundException {
    public ProjectNotFoundException(UUID projectId) {
        super(String.format("Project with id '%s' is not found", projectId));
    }
}

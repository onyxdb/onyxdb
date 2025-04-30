package com.onyxdb.platform.mdb.exceptions;

import java.util.UUID;

public class ProjectNotFoundException extends NotFoundException {
    public ProjectNotFoundException(UUID projectId) {
        super(buildMessage(projectId));
    }

    public static String buildMessage(UUID projectId) {
        return String.format("Project with id '%s' is not found", projectId);
    }
}

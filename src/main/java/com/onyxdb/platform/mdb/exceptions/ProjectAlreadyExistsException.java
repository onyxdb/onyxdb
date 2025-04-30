package com.onyxdb.platform.mdb.exceptions;

public class ProjectAlreadyExistsException extends BadRequestException {
    public ProjectAlreadyExistsException(String projectName) {
        super(buildMessage(projectName));
    }

    public static String buildMessage(String projectName) {
        return String.format("Project with name '%s' already exists", projectName);
    }
}

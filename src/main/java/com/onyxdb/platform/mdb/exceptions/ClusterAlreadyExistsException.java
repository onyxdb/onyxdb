package com.onyxdb.platform.mdb.exceptions;

public class ClusterAlreadyExistsException extends BadRequestException {
    public ClusterAlreadyExistsException(String projectName) {
        super(buildMessage(projectName));
    }

    public static String buildMessage(String projectName) {
        return String.format("Cluster with name '%s' already exists", projectName);
    }
}

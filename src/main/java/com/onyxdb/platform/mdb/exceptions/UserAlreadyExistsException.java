package com.onyxdb.platform.mdb.exceptions;

public class UserAlreadyExistsException extends BadRequestException {
    public UserAlreadyExistsException(String projectName) {
        super(buildMessage(projectName));
    }

    public static String buildMessage(String userName) {
        return String.format("User with name '%s' already exists", userName);
    }
}

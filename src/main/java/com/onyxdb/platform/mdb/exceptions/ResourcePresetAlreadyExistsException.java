package com.onyxdb.platform.mdb.exceptions;

public class ResourcePresetAlreadyExistsException extends BadRequestException {
    public ResourcePresetAlreadyExistsException(String resourcePresetId) {
        super(buildMessage(resourcePresetId));
    }

    public static String buildMessage(String resourcePresetId) {
        return String.format("Resource preset with id '%s' already exists", resourcePresetId);
    }
}

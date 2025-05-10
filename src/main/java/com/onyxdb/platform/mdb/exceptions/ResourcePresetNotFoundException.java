package com.onyxdb.platform.mdb.exceptions;

public class ResourcePresetNotFoundException extends NotFoundException {
    public ResourcePresetNotFoundException(String resourcePresetId) {
        super(buildMessage(resourcePresetId));
    }

    public static String buildMessage(String resourcePresetId) {
        return String.format("Resource preset with id '%s' is not found", resourcePresetId);
    }
}

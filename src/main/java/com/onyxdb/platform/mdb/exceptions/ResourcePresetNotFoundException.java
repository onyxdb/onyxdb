package com.onyxdb.platform.mdb.exceptions;

import java.util.UUID;

public class ResourcePresetNotFoundException extends NotFoundException {
    public ResourcePresetNotFoundException(UUID resourcePresetId) {
        super(buildMessage(resourcePresetId));
    }

    public static String buildMessage(UUID resourcePresetId) {
        return String.format("Resource preset with id '%s' is not found", resourcePresetId);
    }
}

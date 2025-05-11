package com.onyxdb.platform.idm.common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.onyxdb.platform.idm.models.exceptions.ForbiddenException;

public class PermissionCheckUtils {
    public static List<String> generatePermissionKeys(String entity, String action, UUID resourceId) {
        List<String> keys = new ArrayList<>();

        if (resourceId != null) {
            keys.add(String.format("%s-%s-%s", entity, action, resourceId));
            keys.add(String.format("%s-%s-any", entity, action));
        }

        keys.addAll(List.of(
                String.format("%s-%s", entity, action),
                String.format("%s-any", entity),
                String.format("global-%s", action),
                "global-any"
        ));

        return keys;
    }
}

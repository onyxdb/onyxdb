package com.onyxdb.platform.mdb.models;

import java.util.UUID;

public record Host(
        String name,
        UUID clusterId
) {
}

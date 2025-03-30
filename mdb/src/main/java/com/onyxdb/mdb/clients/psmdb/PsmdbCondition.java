package com.onyxdb.mdb.clients.psmdb;

import java.time.LocalDateTime;

public record PsmdbCondition(
        LocalDateTime lastTransitionTime,
        boolean status,
        String type
) {
}

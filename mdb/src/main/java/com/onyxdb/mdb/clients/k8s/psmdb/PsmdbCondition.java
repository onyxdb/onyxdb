package com.onyxdb.mdb.clients.k8s.psmdb;

import java.time.LocalDateTime;

public record PsmdbCondition(
        LocalDateTime lastTransitionTime,
        boolean status,
        String type
) {
}

package com.onyxdb.platform.clients.k8s.psmdb;

public record Psmdb(
        String namespace,
        String name,
        PsmdbSpec spec
) {
}

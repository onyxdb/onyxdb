package com.onyxdb.mdb.clients.k8s.psmdb;

public record Psmdb(
        String namespace,
        String name,
        PsmdbSpec spec
) {
}

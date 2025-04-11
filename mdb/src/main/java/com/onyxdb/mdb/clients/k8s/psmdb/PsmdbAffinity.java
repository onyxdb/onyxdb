package com.onyxdb.mdb.clients.k8s.psmdb;

public record PsmdbAffinity(
        io.fabric8.kubernetes.api.model.Affinity advanced
) {
}

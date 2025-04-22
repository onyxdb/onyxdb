package com.onyxdb.platform.clients.k8s.psmdb;

public record PsmdbAffinity(
        io.fabric8.kubernetes.api.model.Affinity advanced
) {
}

package com.onyxdb.mdb.clients.k8s.psmdb;

import java.util.List;

import io.fabric8.kubernetes.api.model.ResourceRequirements;

public record PsmdbReplset(
        String name,
        int size,
        PsmdbAffinity affinity,
        ResourceRequirements resources,
        PsmdbVolumeSpec volumeSpec,
        List<PsmdbSidecar> sidecars
) {
}

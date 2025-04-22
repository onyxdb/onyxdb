package com.onyxdb.platform.clients.k8s.psmdb;

import io.fabric8.kubernetes.api.model.PersistentVolumeClaimSpec;

public record PsmdbVolumeSpec(
        PersistentVolumeClaimSpec persistentVolumeClaim
) {
}

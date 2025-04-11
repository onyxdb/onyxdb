package com.onyxdb.mdb.clients.k8s.psmdb;

import io.fabric8.kubernetes.api.model.PersistentVolumeClaimSpec;

public record PsmdbVolumeSpec(
        PersistentVolumeClaimSpec persistentVolumeClaim
) {
}

package com.onyxdb.mdb.clients.k8s.psmdb;

import java.util.List;

import io.fabric8.kubernetes.api.model.EnvVar;

public record PsmdbSidecar(
        String name,
        String image,
        List<EnvVar> env,
        List<String> args
) {
}

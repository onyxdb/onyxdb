package com.onyxdb.mdb.clients.k8s.psmdb;

import java.util.List;
import java.util.Map;

import io.fabric8.kubernetes.api.model.EnvVarBuilder;
import io.fabric8.kubernetes.api.model.EnvVarSourceBuilder;
import io.fabric8.kubernetes.api.model.ObjectFieldSelectorBuilder;
import io.fabric8.kubernetes.api.model.SecretKeySelectorBuilder;

public record PsmdbExporterSidecar(
        String image,
        String mongoUserSecretName,
        String mongoUserSecretKey,
        String mongoPasswordSecretName,
        String mongoPasswordSecretKey
) {
    public Map<String, Object> asMap() {
        return Map.ofEntries(
                Map.entry("name", "exporter"),
                Map.entry("image", image),
                Map.entry("env", List.of(
                        new EnvVarBuilder()
                                .withName("EXPORTER_USER")
                                .withValueFrom(
                                        new EnvVarSourceBuilder()
                                                .withSecretKeyRef(
                                                        new SecretKeySelectorBuilder()
                                                                .withName(mongoUserSecretName)
                                                                .withKey(mongoUserSecretKey)
                                                                .build()
                                                )
                                                .build()
                                )
                                .build(),
                        new EnvVarBuilder()
                                .withName("EXPORTER_PASSWORD")
                                .withValueFrom(
                                        new EnvVarSourceBuilder()
                                                .withSecretKeyRef(
                                                        new SecretKeySelectorBuilder()
                                                                .withName(mongoPasswordSecretName)
                                                                .withKey(mongoPasswordSecretKey)
                                                                .build()
                                                )
                                                .build()
                                )
                                .build(),
                        new EnvVarBuilder()
                                .withName("POD_IP")
                                .withValueFrom(
                                        new EnvVarSourceBuilder()
                                                .withFieldRef(
                                                        new ObjectFieldSelectorBuilder()
                                                                .withFieldPath("status.podIP")
                                                                .build()
                                                )
                                                .build()
                                )
                                .build(),
                        new EnvVarBuilder()
                                .withName("MONGODB_URI")
                                .withValue("mongodb://$(EXPORTER_USER):$(EXPORTER_PASSWORD)@$(POD_IP):27017")
                                .build()
                )),
                Map.entry("args", List.of(
                        "--discovering-mode",
                        "--compatible-mode",
                        "--collect-all",
                        "--mongodb.uri=$(MONGODB_URI)"
                ))
        );
    }
}

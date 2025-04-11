package com.onyxdb.mdb.clients.k8s.psmdb;

import java.util.List;
import java.util.Map;

import io.fabric8.kubernetes.api.model.EnvVarBuilder;
import io.fabric8.kubernetes.api.model.EnvVarSourceBuilder;
import io.fabric8.kubernetes.api.model.NodeAffinityBuilder;
import io.fabric8.kubernetes.api.model.NodeSelectorBuilder;
import io.fabric8.kubernetes.api.model.NodeSelectorRequirementBuilder;
import io.fabric8.kubernetes.api.model.NodeSelectorTermBuilder;
import io.fabric8.kubernetes.api.model.ObjectFieldSelectorBuilder;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaimSpecBuilder;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ResourceRequirementsBuilder;
import io.fabric8.kubernetes.api.model.SecretKeySelectorBuilder;
import io.fabric8.kubernetes.api.model.VolumeResourceRequirementsBuilder;

public record PsmdbSpec(
        String crVersion,
        String image,
        String imagePullPolicy,
        PmdbSecrets secrets,
        List<PsmdbReplset> replsets
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        public PsmdbSpec build(String name) {
            return new PsmdbSpec(
                    "1.19.1",
                    "percona/percona-server-mongodb:7.0.15-9-multi",
                    "IfNotPresent",
                    new PmdbSecrets(PsmdbFactory.getPreparedSecretName(name)),
                    List.of(
                            new PsmdbReplset(
                                    "rs-0",
                                    3,
                                    new PsmdbAffinity(
                                            new io.fabric8.kubernetes.api.model.AffinityBuilder()
                                                    .withNodeAffinity(
                                                            new NodeAffinityBuilder()
                                                                    .withRequiredDuringSchedulingIgnoredDuringExecution(
                                                                            new NodeSelectorBuilder()
                                                                                    .withNodeSelectorTerms(
                                                                                            new NodeSelectorTermBuilder()
                                                                                                    .withMatchExpressions(
                                                                                                            new NodeSelectorRequirementBuilder()
                                                                                                                    .withKey("kubernetes.io/hostname")
                                                                                                                    .withOperator("In")
                                                                                                                    .withValues("minikube")
                                                                                                                    .build()
                                                                                                    )
                                                                                                    .build()
                                                                                    )
                                                                                    .build()
                                                                    )
                                                                    .build()
                                                    )
                                                    .build()
                                    ),
                                    new ResourceRequirementsBuilder()
                                            .withRequests(
                                                    Map.ofEntries(
                                                            Map.entry("cpu", Quantity.parse("0.3")),
                                                            Map.entry("memory", Quantity.parse("0.5G"))
                                                    )
                                            )
                                            .build(),
                                    new PsmdbVolumeSpec(
                                            new PersistentVolumeClaimSpecBuilder()
                                                    .withResources(
                                                            new VolumeResourceRequirementsBuilder()
                                                                    .withLimits(Map.ofEntries(
                                                                            Map.entry("storage", Quantity.parse("1G"))
                                                                    ))
                                                                    .withRequests(Map.ofEntries(
                                                                            Map.entry("storage", Quantity.parse("1G"))
                                                                    ))
                                                                    .build()
                                                    )
                                                    .build()
                                    ),
                                    List.of(
                                            new PsmdbSidecar(
                                                    "exporter",
                                                    "percona/mongodb_exporter:0.40",
                                                    List.of(
                                                            new EnvVarBuilder()
                                                                    .withName("EXPORTER_USER")
                                                                    .withValueFrom(
                                                                            new EnvVarSourceBuilder()
                                                                                    .withSecretKeyRef(
                                                                                            new SecretKeySelectorBuilder()
                                                                                                    .withName(PsmdbFactory.getPreparedSecretName(name))
                                                                                                    .withKey("MONGODB_CLUSTER_MONITOR_USER")
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
                                                                                                    .withName(PsmdbFactory.getPreparedSecretName(name))
                                                                                                    .withKey("MONGODB_CLUSTER_MONITOR_PASSWORD")
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
                                                    ),
                                                    List.of(
                                                            "--discovering-mode",
                                                            "--compatible-mode",
                                                            "--collect-all",
                                                            "--mongodb.uri=$(MONGODB_URI)"
                                                    )
                                            )
                                    )
                            )
                    )
            );
        }
    }
}

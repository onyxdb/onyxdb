package com.onyxdb.mdb.clients.psmdb;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.GenericKubernetesResourceBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.ResourceDefinitionContext;

public class PsmdbClient {
    private static final String PSMDB_GROUP = "psmdb.percona.com";
    private static final String PSMDB_VERSION = "v1";
    private static final String PSMDB_API_VERSION = PSMDB_GROUP + "/" + PSMDB_VERSION;
    private static final String PSMDB_KIND = "PerconaServerMongoDB";
    private static final String PSMDB_PLURAL = "perconaservermongodbs";

    private static final ResourceDefinitionContext CONTEXT = new ResourceDefinitionContext.Builder()
            .withGroup(PSMDB_GROUP)
            .withVersion(PSMDB_VERSION)
            .withKind(PSMDB_KIND)
            .withPlural(PSMDB_PLURAL)
            .withNamespaced(true)
            .build();
    private static final TypeReference<Map<String, Object>> PROPERTY_TYPE_REF = new TypeReference<>() {
    };
    private static final TypeReference<List<PsmdbCondition>> CONDITIONS_TYPE_REF = new TypeReference<>() {
    };
    private static final String STATUS_STATE_KEY = "state";
    private static final String STATE_READY_VALUE = "ready";
    // TODO support custom namespace
    private static final String DEFAULT_NAMESPACE = "onyxdb";

    private final KubernetesClient kubernetesClient;
    private final ObjectMapper objectMapper;


    public PsmdbClient(
            KubernetesClient kubernetesClient,
            ObjectMapper objectMapper
    ) {
        this.kubernetesClient = kubernetesClient;
        this.objectMapper = objectMapper;
    }

    public boolean isReady(String name) {
        return getStateO(DEFAULT_NAMESPACE, name)
                .stream()
                .anyMatch(state -> state.equals(STATE_READY_VALUE));
    }

    public Optional<String> getStateO(String namespace, String name) {
        Map<String, Object> statusMap = getStatusMap(namespace, name);
        Optional<Object> stateO = Optional.ofNullable(statusMap.get(STATUS_STATE_KEY));

        return stateO.map(v -> objectMapper.convertValue(v, String.class));
    }

    public List<PsmdbCondition> getConditions(String namespace, String name) {
        Map<String, Object> statusMap = getStatusMap(namespace, name);

        return objectMapper.convertValue(statusMap.getOrDefault("conditions", List.of()), CONDITIONS_TYPE_REF);
    }

    private Map<String, Object> getStatusMap(String namespace, String name) {
        GenericKubernetesResource resource = getResource(namespace, name);
        Object statusObj = resource.getAdditionalProperties().getOrDefault("status", Map.of());

        return objectMapper.convertValue(statusObj, PROPERTY_TYPE_REF);
    }

    public void applyManifest(String name) {
        GenericKubernetesResource resource = new GenericKubernetesResourceBuilder()
                .withApiVersion(PSMDB_API_VERSION)
                .withKind(PSMDB_KIND)
                .withNewMetadata()
                .withName(name)
                .endMetadata()
                .addToAdditionalProperties("spec", buildSpec(name))
                .build();

        kubernetesClient.genericKubernetesResources(PSMDB_API_VERSION, PSMDB_KIND)
                .inNamespace(DEFAULT_NAMESPACE)
                .resource(resource)
                .create();
    }

    private GenericKubernetesResource getResource(
            String namespace,
            String name
    ) {
        return kubernetesClient.genericKubernetesResources(CONTEXT)
                .inNamespace(namespace)
                .withName(name)
                .get();
    }

    // TODO support different mongo versions
    // TODO use own secrets from api
    private Map<String, Object> buildSpec(String name) {
        return Map.ofEntries(
                Map.entry("crVersion", "1.19.1"),
                Map.entry("image", "percona/percona-server-mongodb:7.0.15-9-multi"),
                Map.entry("imagePullPolicy", "IfNotPresent"),
                Map.entry("secrets", Map.ofEntries(
                        Map.entry("users", name + "-secrets")
                )),
                Map.entry("replsets", buildReplsets())
        );
    }

    // TODO support custom replica size
    private List<Map<String, Object>> buildReplsets() {
        return List.of(
                Map.ofEntries(
                        Map.entry("name", "rs-0"),
                        Map.entry("size", 3),
                        Map.entry("affinity", buildReplsetAffinity()),
                        Map.entry("resources", buildReplsetResources()),
                        Map.entry("volumeSpec", buildReplsetVolumeSpec())
                )
        );
    }

    // TODO support labels with zones
    private Map<String, Object> buildReplsetAffinity() {
        return Map.ofEntries(
                Map.entry("advanced", Map.ofEntries(
                        Map.entry("nodeAffinity", Map.ofEntries(
                                Map.entry("requiredDuringSchedulingIgnoredDuringExecution", Map.ofEntries(
                                        Map.entry("nodeSelectorTerms", List.of(
                                                        Map.ofEntries(
                                                                Map.entry("matchExpressions", List.of(Map.ofEntries(
                                                                        Map.entry("key", "kubernetes.io/hostname"),
                                                                        Map.entry("operator", "In"),
                                                                        Map.entry("values", List.of(
                                                                                "minikube"
                                                                        )))
                                                                ))
                                                        )
                                                )
                                        ))
                                )
                        ))
                ))
        );
    }

    // TODO support custom resources
    private Map<String, Object> buildReplsetResources() {
        return Map.ofEntries(
                Map.entry("limits", Map.ofEntries(
                        Map.entry("cpu", "0.3"),
                        Map.entry("memory", "0.5G")
                )),
                Map.entry("requests", Map.ofEntries(
                        Map.entry("cpu", "0.3"),
                        Map.entry("memory", "0.5G")
                ))
        );
    }

    // TODO support custom storage
    private Map<String, Object> buildReplsetVolumeSpec() {
        return Map.ofEntries(
                Map.entry("persistentVolumeClaim", Map.ofEntries(
                        Map.entry("resources", Map.ofEntries(
                                Map.entry("requests", Map.ofEntries(
                                        Map.entry("storage", "1Gi")
                                ))
                        ))
                ))
        );
    }
}

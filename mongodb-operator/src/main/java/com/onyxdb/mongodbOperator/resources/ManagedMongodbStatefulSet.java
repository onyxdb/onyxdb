package com.onyxdb.mongodbOperator.resources;

import java.util.List;
import java.util.Map;

import io.fabric8.kubernetes.api.model.EnvVarBuilder;
import io.fabric8.kubernetes.api.model.EnvVarSourceBuilder;
import io.fabric8.kubernetes.api.model.LabelSelector;
import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaimBuilder;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaimSpec;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaimSpecBuilder;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.PodSpecBuilder;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.PodTemplateSpecBuilder;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.fabric8.kubernetes.api.model.ResourceRequirementsBuilder;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretKeySelectorBuilder;
import io.fabric8.kubernetes.api.model.VolumeMountBuilder;
import io.fabric8.kubernetes.api.model.VolumeResourceRequirements;
import io.fabric8.kubernetes.api.model.VolumeResourceRequirementsBuilder;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.apps.StatefulSetBuilder;
import io.fabric8.kubernetes.api.model.apps.StatefulSetSpec;
import io.fabric8.kubernetes.api.model.apps.StatefulSetSpecBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ResourceIDMatcherDiscriminator;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.event.ResourceID;

import com.onyxdb.mongodbOperator.utils.K8sUtils;


/**
 * @author foxleren
 */
public class ManagedMongodbStatefulSet extends CRUDKubernetesDependentResource<StatefulSet, ManagedMongoDB> {
    public static final String DEPENDENT_NAME = "managed-mongodb-stateful-set";

    private static final String RESOURCE_NAME_PREFIX = "managed-mongodb";
    private static final String MONGODB_CONTAINER_NAME = "mongodb";
    private static final String MONGODB_CONTAINER_MOUNT_PATH = "/data/db";
    private static final String PVC_NAME = "managed-mongodb-pvc";

    // TODO configure by manifest
    private static final int DEFAULT_REPLICAS = 3;
    private static final String DEFAULT_STORAGE_CLASS = "standard";
    private static final String DEFAULT_STORAGE_SIZE = "1Gi";

    public ManagedMongodbStatefulSet() {
        super(StatefulSet.class);
    }

    @Override
    protected StatefulSet desired(ManagedMongoDB primary, Context<ManagedMongoDB> context) {
        ObjectMeta meta = K8sUtils.enrichResourceMeta(primary, RESOURCE_NAME_PREFIX);
        return new StatefulSetBuilder()
                .withMetadata(meta)
                .withSpec(buildSpec(primary, meta))
                .build();
    }

    private StatefulSetSpec buildSpec(ManagedMongoDB primary, ObjectMeta meta) {
        return new StatefulSetSpecBuilder()
                .withServiceName(K8sUtils.buildResourceName(RESOURCE_NAME_PREFIX, meta.getName()))
                .withReplicas(DEFAULT_REPLICAS)
                .withSelector(buildSelector(meta.getLabels()))
                .withTemplate(buildPodTemplate(primary, meta))
                .withVolumeClaimTemplates(buildPersistentVolumeClaim(meta.getNamespace()))
                .build();
    }

    private LabelSelector buildSelector(Map<String, String> labels) {
        return new LabelSelectorBuilder()
                .addToMatchLabels(labels)
                .build();
    }

    private PodTemplateSpec buildPodTemplate(ManagedMongoDB primary, ObjectMeta meta) {
        return new PodTemplateSpecBuilder()
                .withMetadata(meta)
                .withSpec(buildPodSpec(primary, meta))
                .build();
    }

    private PodSpec buildPodSpec(ManagedMongoDB primary, ObjectMeta meta) {
        ManagedMongodbSpec primarySpec = primary.getSpec();
        List<String> mongodbContainerCommand = List.of(
                "mongod",
                "--bind_ip_all",
                "--dbpath",
                MONGODB_CONTAINER_MOUNT_PATH,
                "--replSet",
                "rs0"
        );
        ResourceRequirements mongodbContainerResources = new ResourceRequirementsBuilder()
                .withLimits(Map.ofEntries(Map.entry("memory", Quantity.parse("1Gi"))))
                .withRequests(Map.ofEntries(Map.entry("memory", Quantity.parse("1Gi"))))
                .build();
        String managedMongodbSecret = K8sUtils.buildResourceName(RESOURCE_NAME_PREFIX, primary.getMetadata().getName());

        return new PodSpecBuilder()
                .addNewContainer()
                .withName(MONGODB_CONTAINER_NAME)
                .withImage(primarySpec.image())
                .withImagePullPolicy("IfNotPresent")
                .withCommand(mongodbContainerCommand)
                .withResources(mongodbContainerResources)
                .withVolumeMounts(new VolumeMountBuilder()
                        .withMountPath(MONGODB_CONTAINER_MOUNT_PATH)
                        .withName(PVC_NAME)
                        .build()
                )
                .withEnv(new EnvVarBuilder()
                        .withName("MONGO_INITDB_ROOT_USERNAME")
                        .withValueFrom(new EnvVarSourceBuilder()
                                .withSecretKeyRef(new SecretKeySelectorBuilder()
                                        .withKey("user")
                                        .withName(managedMongodbSecret)
                                        .build())
                                .build())
                        .build())
                .withEnv(new EnvVarBuilder()
                        .withName("MONGO_INITDB_ROOT_PASSWORD")
                        .withValueFrom(new EnvVarSourceBuilder()
                                .withSecretKeyRef(new SecretKeySelectorBuilder()
                                        .withKey("password")
                                        .withName(managedMongodbSecret)
                                        .build())
                                .build())
                        .build())
                .and()
                .build();
    }

    private List<PersistentVolumeClaim> buildPersistentVolumeClaim(String namespace) {
        ObjectMeta meta = new ObjectMetaBuilder()
                .withNamespace(namespace)
                .withName(PVC_NAME)
                .build();

        VolumeResourceRequirements resources = new VolumeResourceRequirementsBuilder()
                .addToRequests("storage", Quantity.parse(DEFAULT_STORAGE_SIZE))
                .build();

        PersistentVolumeClaimSpec spec = new PersistentVolumeClaimSpecBuilder()
                .withStorageClassName(DEFAULT_STORAGE_CLASS)
                .withAccessModes(List.of("ReadWriteOnce"))
                .withResources(resources)
                .build();

        return List.of(new PersistentVolumeClaimBuilder()
                .withMetadata(meta)
                .withSpec(spec)
                .build());
    }

    @SuppressWarnings("unused")
    static class Discriminator extends ResourceIDMatcherDiscriminator<Secret, ManagedMongoDB> {
        public Discriminator() {
            super(RESOURCE_NAME_PREFIX, r -> {
                        ObjectMeta meta = r.getMetadata();
                        return new ResourceID(
                                K8sUtils.buildResourceName(RESOURCE_NAME_PREFIX, meta.getName()),
                                meta.getNamespace()
                        );
                    }
            );
        }
    }
}

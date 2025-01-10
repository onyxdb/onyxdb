package com.onyxdb.mongodbOperator.resources;

import java.util.List;
import java.util.Map;

import io.fabric8.kubernetes.api.model.ContainerBuilder;
import io.fabric8.kubernetes.api.model.ContainerPortBuilder;
import io.fabric8.kubernetes.api.model.KeyToPathBuilder;
import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaimBuilder;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaimSpecBuilder;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.PodSpecBuilder;
import io.fabric8.kubernetes.api.model.PodTemplateSpecBuilder;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.fabric8.kubernetes.api.model.ResourceRequirementsBuilder;
import io.fabric8.kubernetes.api.model.SecretVolumeSourceBuilder;
import io.fabric8.kubernetes.api.model.VolumeBuilder;
import io.fabric8.kubernetes.api.model.VolumeMountBuilder;
import io.fabric8.kubernetes.api.model.VolumeResourceRequirementsBuilder;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.api.model.apps.StatefulSetBuilder;
import io.fabric8.kubernetes.api.model.apps.StatefulSetSpec;
import io.fabric8.kubernetes.api.model.apps.StatefulSetSpecBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;

import com.onyxdb.mongodbOperator.discriminators.MongoStatefulSetDiscriminator;
import com.onyxdb.mongodbOperator.utils.MetaUtils;


/**
 * @author foxleren
 */
@KubernetesDependent(resourceDiscriminator = MongoStatefulSetDiscriminator.class)
public class MongoStatefulSet extends CRUDKubernetesDependentResource<StatefulSet, ManagedMongoDB> {
    public static final String DEPENDENT_NAME = "managed-mongodb-stateful-set";
    public static final String MONGODB_CONTAINER_NAME = "mongodb";

    private static final String MONGODB_CONTAINER_MOUNT_PATH = "/data/db";
    public static final String MONGO_KEY_FILE_PATH = "/etc/onyxdb-managed-mongodb/key";
    private static final String PVC_NAME = "managed-mongodb-pvc";
    private static final List<String> PVC_ACCESS_MODES = List.of("ReadWriteOnce");

    // TODO configure by manifest
    private static final int DEFAULT_REPLICAS = 3;
    private static final String DEFAULT_STORAGE_CLASS = "standard";
    private static final String DEFAULT_STORAGE_SIZE = "1Gi";
    public static final int MONGODB_CONTAINER_PORT = 27017;

    private static final String MONGO_INITDB_ROOT_USERNAME_ENV = "MONGO_INITDB_ROOT_USERNAME";
    private static final String MONGO_INITDB_ROOT_PASSWORD_ENV = "MONGO_INITDB_ROOT_PASSWORD";

    public MongoStatefulSet() {
        super(StatefulSet.class);
    }

    @Override
    protected StatefulSet desired(ManagedMongoDB primary, Context<ManagedMongoDB> context) {
        ObjectMeta currentMeta = MetaUtils.createMetaFromPrimary(primary);
        return new StatefulSetBuilder()
                .withMetadata(currentMeta)
                .withSpec(buildSpec(primary, currentMeta))
                .build();
    }

    private StatefulSetSpec buildSpec(ManagedMongoDB primary, ObjectMeta currentMeta) {
        return new StatefulSetSpecBuilder()
                .withServiceName(MetaUtils.getResourceInstanceNameWithPrefix(primary))
                .withReplicas(DEFAULT_REPLICAS)
                .withSelector(new LabelSelectorBuilder()
                        .withMatchLabels(currentMeta.getLabels())
                        .build()
                )
                .withTemplate(new PodTemplateSpecBuilder()
                        .withMetadata(currentMeta)
                        .withSpec(buildPodSpec(primary))
                        .build()
                )
                .withVolumeClaimTemplates(buildPVC(currentMeta.getNamespace()))
                .build();
    }

    private PodSpec buildPodSpec(ManagedMongoDB primary) {
        MongoSpec primarySpec = primary.getSpec();
        List<String> mongodbContainerCommand = List.of(
//                "cat /etc/onyxdb-managed-mongodb/key"
                "mongod",
                "--bind_ip_all",
                "--auth",
                "--dbpath",
                MONGODB_CONTAINER_MOUNT_PATH,
                "--keyFile",
                MONGO_KEY_FILE_PATH,
                "--replSet",
                "rs0"
        );

        ResourceRequirements mongodbContainerResources = new ResourceRequirementsBuilder()
                .withLimits(Map.ofEntries(Map.entry("memory", Quantity.parse("1Gi"))))
                .withRequests(Map.ofEntries(Map.entry("memory", Quantity.parse("1Gi"))))
                .build();

        String managedMongodbSecret = MetaUtils.getResourceInstanceNameWithPrefix(primary);

        return new PodSpecBuilder()
                .withContainers(new ContainerBuilder()
                        .withName(MONGODB_CONTAINER_NAME)
                        .withImage(primarySpec.image())
                        .withImagePullPolicy("IfNotPresent")
                        .withCommand(mongodbContainerCommand)
                        .withResources(mongodbContainerResources)
                        .withPorts(
                                new ContainerPortBuilder()
                                        .withContainerPort(MONGODB_CONTAINER_PORT)
                                        .build()
                        )
                        .withVolumeMounts(
                                new VolumeMountBuilder()
                                        .withName(PVC_NAME)
                                        .withMountPath(MONGODB_CONTAINER_MOUNT_PATH)
                                        .build(),
                                new VolumeMountBuilder()
                                        .withName("mongo-key")
                                        .withMountPath("/etc/onyxdb-managed-mongodb")
                                        .build()
                        ).build())
                .withVolumes(
                        new VolumeBuilder()
                                .withName("mongo-key")
                                .withSecret(new SecretVolumeSourceBuilder()
                                        .withSecretName(managedMongodbSecret)
                                        .withItems(List.of(new KeyToPathBuilder()
                                                .withKey("key")
                                                .withPath("key")
                                                .withMode(256)
                                                .build()))
                                        .build())
                                .build()
                )
                .build();
    }

    private List<PersistentVolumeClaim> buildPVC(String namespace) {
        var meta = new ObjectMetaBuilder()
                .withNamespace(namespace)
                .withName(PVC_NAME)
                .build();

        var resources = new VolumeResourceRequirementsBuilder()
                .addToRequests("storage", Quantity.parse(DEFAULT_STORAGE_SIZE))
                .build();

        var spec = new PersistentVolumeClaimSpecBuilder()
                .withStorageClassName(DEFAULT_STORAGE_CLASS)
                .withAccessModes(PVC_ACCESS_MODES)
                .withResources(resources)
                .build();

        return List.of(new PersistentVolumeClaimBuilder()
                .withMetadata(meta)
                .withSpec(spec)
                .build());
    }
}

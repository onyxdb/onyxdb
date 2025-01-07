package com.onyxdb.mongodbOperator.resources;

import java.util.List;
import java.util.Map;

import io.fabric8.kubernetes.api.model.ContainerPortBuilder;
import io.fabric8.kubernetes.api.model.EnvVarBuilder;
import io.fabric8.kubernetes.api.model.EnvVarSourceBuilder;
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
import io.fabric8.kubernetes.api.model.SecretKeySelectorBuilder;
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
import com.onyxdb.mongodbOperator.utils.K8sUtils;
import com.onyxdb.mongodbOperator.utils.LabelsUtils;


/**
 * @author foxleren
 */
@KubernetesDependent(resourceDiscriminator = MongoStatefulSetDiscriminator.class)
public class MongoStatefulSet extends CRUDKubernetesDependentResource<StatefulSet, ManagedMongoDB> {
    public static final String DEPENDENT_NAME = "managed-mongodb-stateful-set";
    public static final String MONGODB_CONTAINER_NAME = "mongodb";

    private static final String MONGODB_CONTAINER_MOUNT_PATH = "/data/db";
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
        ObjectMeta currentMeta = K8sUtils.createMetaFromPrimary(primary);
        return new StatefulSetBuilder()
                .withMetadata(currentMeta)
                .withSpec(buildSpec(primary, currentMeta))
                .build();
    }

    private StatefulSetSpec buildSpec(ManagedMongoDB primary, ObjectMeta currentMeta) {
        return new StatefulSetSpecBuilder()
                .withServiceName(currentMeta.getName())
                .withReplicas(DEFAULT_REPLICAS)
                .withSelector(new LabelSelectorBuilder()
                        .addToMatchLabels(LabelsUtils.getClusterLabels(currentMeta.getName()))
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

        String managedMongodbSecret = K8sUtils.getResourceInstanceNameWithPrefix(primary);

        return new PodSpecBuilder()
                .addNewContainer()
                .withName(MONGODB_CONTAINER_NAME)
                .withImage(primarySpec.image())
                .withImagePullPolicy("IfNotPresent")
                .withCommand(mongodbContainerCommand)
                .withResources(mongodbContainerResources)
                .withPorts(new ContainerPortBuilder()
                        .withContainerPort(MONGODB_CONTAINER_PORT)
                        .build()
                )
                .withVolumeMounts(new VolumeMountBuilder()
                        .withMountPath(MONGODB_CONTAINER_MOUNT_PATH)
                        .withName(PVC_NAME)
                        .build()
                )
                .withEnv(new EnvVarBuilder()
                                .withName(MONGO_INITDB_ROOT_USERNAME_ENV)
                                .withValueFrom(new EnvVarSourceBuilder()
                                        .withSecretKeyRef(new SecretKeySelectorBuilder()
                                                .withKey("user")
                                                .withName(managedMongodbSecret)
                                                .build())
                                        .build())
                                .build(),
                        new EnvVarBuilder()
                                .withName(MONGO_INITDB_ROOT_PASSWORD_ENV)
                                .withValueFrom(new EnvVarSourceBuilder()
                                        .withSecretKeyRef(new SecretKeySelectorBuilder()
                                                .withKey("password")
                                                .withName(managedMongodbSecret)
                                                .build())
                                        .build())
                                .build()
                )
                .and()
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

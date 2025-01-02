package com.onyxdb.mongodbOperator.crds;

import java.util.List;
import java.util.Map;

import io.fabric8.kubernetes.api.model.LabelSelector;
import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaimBuilder;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaimSpecBuilder;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.PodSpecBuilder;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.PodTemplateSpecBuilder;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.Service;
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

import com.onyxdb.mongodbOperator.utils.K8sManifestHelper;

import static com.onyxdb.mongodbOperator.utils.K8sManifestHelper.fromPrimary;


/**
 * @author foxleren
 */
public class ManagedMongodbStatefulSetResource extends CRUDKubernetesDependentResource<StatefulSet, ManagedMongodbResource> {
    public static final String COMPONENT = "managed-mongodb-stateful-set";

    private static final String TEMPLATE_PATH = "templates/mongodb-stateful-set.yaml";
    // TODO configure by manifest
    private static final int REPLICAS = 3;

    private final StatefulSet template;

    public ManagedMongodbStatefulSetResource() {
        super(StatefulSet.class);
        this.template = K8sManifestHelper.loadTemplate(StatefulSet.class, TEMPLATE_PATH);
    }

    @Override
    protected StatefulSet desired(ManagedMongodbResource primary, Context<ManagedMongodbResource> context) {
        ObjectMeta meta = fromPrimary(primary, COMPONENT)
                .build();

        return new StatefulSetBuilder(template)
                .withMetadata(meta)
                .withSpec(buildSpec(primary, meta))
                .build();
    }

    private StatefulSetSpec buildSpec(ManagedMongodbResource primary, ObjectMeta primaryMeta) {
        return new StatefulSetSpecBuilder()
                .withSelector(buildSelector(primaryMeta.getLabels()))
                .withReplicas(REPLICAS)
                .withTemplate(buildPodTemplate(primary, primaryMeta))
                .withVolumeClaimTemplates(buildPersistentVolumeClaim(primary))
                .build();
    }

    private LabelSelector buildSelector(Map<String, String> labels) {
        return new LabelSelectorBuilder()
                .addToMatchLabels(labels)
                .build();
    }

    private PodTemplateSpec buildPodTemplate(ManagedMongodbResource primary, ObjectMeta primaryMeta) {
        return new PodTemplateSpecBuilder()
                .withMetadata(primaryMeta)
                .withSpec(buildPodSpec(primary))
                .build();
    }

    private List<PersistentVolumeClaim> buildPersistentVolumeClaim(ManagedMongodbResource primary) {
//        primary.getSpec().getDiskSizeBytes();
        return List.of(
                new PersistentVolumeClaimBuilder()
                        .withMetadata(new ObjectMetaBuilder()
                                .withNamespace("onyxdb")
                                .withName("onyxdb-managed-mongodb-pvc")
                                .build())
                        .withSpec(new PersistentVolumeClaimSpecBuilder()
                                .withStorageClassName("standard")
                                .withAccessModes(List.of("ReadWriteOnce"))
                                .withResources(new VolumeResourceRequirementsBuilder()
                                        .addToRequests("storage", Quantity.parse("1Gi"))
                                        .build())
                                .build())
                        .build()
        );
    }

    private PodSpec buildPodSpec(ManagedMongodbResource primary) {
        // TODO configure image by manifest
//        String imageVersion = StringUtils.hasText(primary.getSpec().getApiServerVersion()) ?
//                ":" + primary.getSpec().getApiServerVersion().trim() : "";
//
//        String imageName = StringUtils.hasText(primary.getSpec().getApiServerImage()) ?
//                primary.getSpec().getApiServerImage().trim() : Constants.DEFAULT_API_SERVER_IMAGE;

        return new PodSpecBuilder(template.getSpec().getTemplate().getSpec())
                .editContainer(0)
//                .withImage(imageName + imageVersion)
                .and()
                .build();
    }

    @SuppressWarnings("unused")
    static class Discriminator extends ResourceIDMatcherDiscriminator<Service, ManagedMongodbResource> {
        public Discriminator() {
            super(COMPONENT, (p) -> new ResourceID(p.getMetadata().getName() + "-" + COMPONENT, p.getMetadata().getNamespace()));
        }
    }
}

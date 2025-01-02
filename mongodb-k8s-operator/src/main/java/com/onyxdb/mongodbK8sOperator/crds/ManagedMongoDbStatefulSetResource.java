package com.onyxdb.mongodbK8sOperator.crds;

import java.util.Map;

import io.fabric8.kubernetes.api.model.LabelSelector;
import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.PodSpecBuilder;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.PodTemplateSpecBuilder;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpecBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;

import com.onyxdb.mongodbK8sOperator.utils.K8sManifestHelper;

import static com.onyxdb.mongodbK8sOperator.utils.K8sManifestHelper.fromPrimary;


/**
 * @author foxleren
 */
public class ManagedMongoDbStatefulSetResource extends CRUDKubernetesDependentResource<Deployment, ManagedMongoDbResource> {
    public static final String COMPONENT = "managed-mongodb-stateful-set";

    private static final String TEMPLATE_PATH = "templates/mongodb-stateful-set.yaml";
    // TODO configure by manifest
    private static final int REPLICAS = 3;

    private final Deployment template;

    public ManagedMongoDbStatefulSetResource() {
        super(Deployment.class);
        this.template = K8sManifestHelper.loadTemplate(Deployment.class, TEMPLATE_PATH);
    }

    @Override
    protected Deployment desired(ManagedMongoDbResource primary, Context<ManagedMongoDbResource> context) {
        ObjectMeta meta = fromPrimary(primary, COMPONENT)
                .build();

        return new DeploymentBuilder(template)
                .withMetadata(meta)
                .withSpec(buildSpec(primary, meta))
                .build();
    }

    private DeploymentSpec buildSpec(ManagedMongoDbResource primary, ObjectMeta primaryMeta) {
        return new DeploymentSpecBuilder()
                .withSelector(buildSelector(primaryMeta.getLabels()))
                .withReplicas(REPLICAS)
                .withTemplate(buildPodTemplate(primary, primaryMeta))
                .build();
    }

    private LabelSelector buildSelector(Map<String, String> labels) {
        return new LabelSelectorBuilder()
                .addToMatchLabels(labels)
                .build();
    }

    private PodTemplateSpec buildPodTemplate(ManagedMongoDbResource primary, ObjectMeta primaryMeta) {
        return new PodTemplateSpecBuilder()
                .withMetadata(primaryMeta)
                .withSpec(buildPodSpec(primary))
                .build();
    }

    private PodSpec buildPodSpec(ManagedMongoDbResource primary) {
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
}

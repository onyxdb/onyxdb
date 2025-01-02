package com.onyxdb.mongodbK8sOperator.crds;

import java.util.HashMap;
import java.util.Map;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ResourceIDMatcherDiscriminator;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.event.ResourceID;

import com.onyxdb.mongodbK8sOperator.utils.K8sManifestHelper;

import static com.onyxdb.mongodbK8sOperator.utils.K8sManifestHelper.fromPrimary;

/**
 * @author foxleren
 */
public class ManagedMongoDbServiceResource extends CRUDKubernetesDependentResource<Service, ManagedMongoDbResource> {
    public static final String COMPONENT = "managed-mongodb-service";

    private final Service template;

    public ManagedMongoDbServiceResource() {
        super(Service.class);
        this.template = K8sManifestHelper.loadTemplate(Service.class, "templates/mongodb-service.yaml");
    }

    @Override
    protected Service desired(ManagedMongoDbResource primary, Context<ManagedMongoDbResource> context) {
        ObjectMeta meta = fromPrimary(primary, COMPONENT)
                .build();

        Map<String, String> selector = new HashMap<>(meta.getLabels());
        selector.put("component", ManagedMongoDbStatefulSetResource.COMPONENT);

        return new ServiceBuilder(template)
                .withMetadata(meta)
                .editSpec()
                .withSelector(selector)
                .endSpec()
                .build();
    }

    @SuppressWarnings("unused")
    static class Discriminator extends ResourceIDMatcherDiscriminator<Service, ManagedMongoDbResource> {
        public Discriminator() {
            super(COMPONENT, (p) -> new ResourceID(p.getMetadata().getName() + "-" + COMPONENT, p.getMetadata().getNamespace()));
        }
    }
}

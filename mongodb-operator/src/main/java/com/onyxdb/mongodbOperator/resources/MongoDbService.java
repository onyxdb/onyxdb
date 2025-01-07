package com.onyxdb.mongodbOperator.resources;

import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.ServicePortBuilder;
import io.fabric8.kubernetes.api.model.ServiceSpec;
import io.fabric8.kubernetes.api.model.ServiceSpecBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ResourceIDMatcherDiscriminator;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.event.ResourceID;

import com.onyxdb.mongodbOperator.utils.K8sUtils;
import com.onyxdb.mongodbOperator.utils.LabelsUtil;

/**
 * @author foxleren
 */
public class MongoDbService extends CRUDKubernetesDependentResource<Service, ManagedMongoDB> {
    public static final String DEPENDENT_NAME = "managed-mongodb-service";

    private static final String RESOURCE_NAME_PREFIX = "managed-mongodb";

    public MongoDbService() {
        super(Service.class);
    }

    @Override
    protected Service desired(ManagedMongoDB primary, Context<ManagedMongoDB> context) {
        return new ServiceBuilder()
                .withMetadata(K8sUtils.enrichResourceMeta(primary, RESOURCE_NAME_PREFIX))
                .withSpec(buildServiceSpec(primary))
                .build();
    }

    public static String getServiceName(String primaryName) {
        return K8sUtils.buildResourceName(RESOURCE_NAME_PREFIX, primaryName);
    }

    private ServiceSpec buildServiceSpec(ManagedMongoDB primary) {
        return new ServiceSpecBuilder()
                .withSelector(LabelsUtil.getClusterLabels(primary.getMetadata().getName()))
                .withType("ClusterIP")
                .withClusterIP("None")
                .withPorts(new ServicePortBuilder()
                        .withProtocol("TCP")
                        .withPort(27017)
                        .withTargetPort(new IntOrString(27017))
                        .build()
                ).build();
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

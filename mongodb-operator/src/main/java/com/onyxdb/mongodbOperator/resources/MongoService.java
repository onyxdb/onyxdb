package com.onyxdb.mongodbOperator.resources;

import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.ServicePortBuilder;
import io.fabric8.kubernetes.api.model.ServiceSpec;
import io.fabric8.kubernetes.api.model.ServiceSpecBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;

import com.onyxdb.mongodbOperator.discriminators.MongoServiceDiscriminator;
import com.onyxdb.mongodbOperator.utils.LabelsUtils;
import com.onyxdb.mongodbOperator.utils.MetaUtils;

/**
 * @author foxleren
 */
@KubernetesDependent(resourceDiscriminator = MongoServiceDiscriminator.class)
public class MongoService extends CRUDKubernetesDependentResource<Service, ManagedMongoDB> {
    public static final String DEPENDENT_NAME = "managed-mongodb-service";

    public MongoService() {
        super(Service.class);
    }

    @Override
    protected Service desired(ManagedMongoDB primary, Context<ManagedMongoDB> context) {
        return new ServiceBuilder()
                .withMetadata(MetaUtils.createMetaFromPrimary(primary))
                .withSpec(buildServiceSpec(primary))
                .build();
    }

    private ServiceSpec buildServiceSpec(ManagedMongoDB primary) {
        return new ServiceSpecBuilder()
                .withSelector(LabelsUtils.getClusterLabels(primary))
                .withType("ClusterIP")
                .withClusterIP("None")
                .withPorts(new ServicePortBuilder()
                        .withProtocol("TCP")
                        .withPort(MongoStatefulSet.MONGODB_CONTAINER_PORT)
                        .withTargetPort(new IntOrString(MongoStatefulSet.MONGODB_CONTAINER_PORT))
                        .build()
                ).build();
    }
}

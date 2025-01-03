package com.onyxdb.mongodbOperator.resources;

import java.util.Map;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ResourceIDMatcherDiscriminator;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.event.ResourceID;

import com.onyxdb.mongodbOperator.utils.K8sUtils;

/**
 * @author foxleren
 */
public class ManagedMongoDBSecret extends CRUDKubernetesDependentResource<Secret, ManagedMongoDB> {
    public static final String DEPENDENT_NAME = "managed-mongodb-secret";

    private static final String RESOURCE_NAME_PREFIX = "managed-mongodb";

    public ManagedMongoDBSecret() {
        super(Secret.class);
    }

    @Override
    protected Secret desired(ManagedMongoDB primary, Context<ManagedMongoDB> context) {
        RootUser rootUser = primary.getSpec().rootUser();
        Map<String, String> stringData = Map.ofEntries(
                Map.entry("user", rootUser.user()),
                Map.entry("password", rootUser.password())
        );

        return new SecretBuilder()
                .withMetadata(K8sUtils.enrichResourceMeta(primary, RESOURCE_NAME_PREFIX))
                .withType("Opaque")
                .withStringData(stringData)
                .build();
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

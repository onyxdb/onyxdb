package com.onyxdb.mongodbOperator.resources;

import java.util.Map;

import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;

import com.onyxdb.mongodbOperator.discriminators.MongoSecretDiscriminator;
import com.onyxdb.mongodbOperator.utils.K8sUtils;

/**
 * @author foxleren
 */
@KubernetesDependent(resourceDiscriminator = MongoSecretDiscriminator.class)
public class MongoSecret extends CRUDKubernetesDependentResource<Secret, ManagedMongoDB> {
    public static final String DEPENDENT_NAME = "managed-mongodb-secret";

    public MongoSecret() {
        super(Secret.class);
    }

    @Override
    protected Secret desired(ManagedMongoDB primary, Context<ManagedMongoDB> context) {
        MongoUser initUser = primary.getSpec().initUser();
        Map<String, String> stringData = Map.ofEntries(
                Map.entry("user", initUser.user()),
                Map.entry("password", initUser.password())
        );

        return new SecretBuilder()
                .withMetadata(K8sUtils.createMetaFromPrimary(primary))
                .withType("Opaque")
                .withStringData(stringData)
                .build();
    }
}

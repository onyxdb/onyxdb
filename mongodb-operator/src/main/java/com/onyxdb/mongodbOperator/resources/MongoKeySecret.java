package com.onyxdb.mongodbOperator.resources;

import java.util.Map;

import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.KubernetesDependent;

import com.onyxdb.mongodbOperator.discriminators.MongoKeySecretDiscriminator;
import com.onyxdb.mongodbOperator.discriminators.MongoSecretDiscriminator;
import com.onyxdb.mongodbOperator.utils.MetaUtils;

/**
 * @author foxleren
 */
@KubernetesDependent(resourceDiscriminator = MongoKeySecretDiscriminator.class)
public class MongoKeySecret extends CRUDKubernetesDependentResource<Secret, ManagedMongoDB> {
    public static final String DEPENDENT_NAME = "managed-mongodb-key-secret";

    public MongoKeySecret() {
        super(Secret.class);
    }

    @Override
    protected Secret desired(ManagedMongoDB primary, Context<ManagedMongoDB> context) {

//        MongoUser initUser = primary.getSpec().initUser();
        Map<String, String> stringData = Map.ofEntries(Map.entry("key", "key_value"));

        return new SecretBuilder()
                .withMetadata(MetaUtils.createMetaFromPrimary(primary))
                .withType("Opaque")
                .withStringData(stringData)
                .build();
    }
}

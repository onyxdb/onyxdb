package com.onyxdb.mongodbOperator.discriminators;

import io.fabric8.kubernetes.api.model.Secret;
import io.javaoperatorsdk.operator.api.reconciler.ResourceIDMatcherDiscriminator;
import io.javaoperatorsdk.operator.processing.event.ResourceID;

import com.onyxdb.mongodbOperator.resources.ManagedMongoDB;
import com.onyxdb.mongodbOperator.resources.MongoDbSecret;
import com.onyxdb.mongodbOperator.utils.K8sUtils;

/**
 * @author foxleren
 */
public class MongoDBSecretDiscriminator extends ResourceIDMatcherDiscriminator<Secret, ManagedMongoDB> {
    public MongoDBSecretDiscriminator() {
        super(MongoDbSecret.DEPENDENT_NAME, p -> {
            String name = K8sUtils.buildResourceName(MongoDbSecret.DEPENDENT_NAME, p.getMetadata().getName());
            return new ResourceID(name, p.getMetadata().getNamespace());
        });
    }
}

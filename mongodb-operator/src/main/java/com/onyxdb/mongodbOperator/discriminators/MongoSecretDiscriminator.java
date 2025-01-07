package com.onyxdb.mongodbOperator.discriminators;

import io.fabric8.kubernetes.api.model.Secret;
import io.javaoperatorsdk.operator.api.reconciler.ResourceIDMatcherDiscriminator;
import io.javaoperatorsdk.operator.processing.event.ResourceID;

import com.onyxdb.mongodbOperator.resources.ManagedMongoDB;
import com.onyxdb.mongodbOperator.resources.MongoSecret;
import com.onyxdb.mongodbOperator.utils.K8sUtil;

/**
 * @author foxleren
 */
public class MongoSecretDiscriminator extends ResourceIDMatcherDiscriminator<Secret, ManagedMongoDB> {
    public MongoSecretDiscriminator() {
        super(
                MongoSecret.DEPENDENT_NAME,
                primary -> new ResourceID(
                        K8sUtil.getResourceInstanceNameWithPrefix(primary),
                        primary.getMetadata().getNamespace()
                )
        );
    }
}

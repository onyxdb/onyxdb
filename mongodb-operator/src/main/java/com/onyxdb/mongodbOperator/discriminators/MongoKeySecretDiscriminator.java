package com.onyxdb.mongodbOperator.discriminators;

import io.fabric8.kubernetes.api.model.Secret;
import io.javaoperatorsdk.operator.api.reconciler.ResourceIDMatcherDiscriminator;
import io.javaoperatorsdk.operator.processing.event.ResourceID;

import com.onyxdb.mongodbOperator.resources.ManagedMongoDB;
import com.onyxdb.mongodbOperator.resources.MongoKeySecret;
import com.onyxdb.mongodbOperator.utils.MetaUtils;

/**
 * @author foxleren
 */
public class MongoKeySecretDiscriminator extends ResourceIDMatcherDiscriminator<Secret, ManagedMongoDB> {
    public MongoKeySecretDiscriminator() {
        super(
                MongoKeySecret.DEPENDENT_NAME,
                primary -> new ResourceID(
                        MetaUtils.getResourceInstanceNameWithPrefix(primary),
                        primary.getMetadata().getNamespace()
                )
        );
    }
}

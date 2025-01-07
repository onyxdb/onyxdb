package com.onyxdb.mongodbOperator.discriminators;

import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.javaoperatorsdk.operator.api.reconciler.ResourceIDMatcherDiscriminator;
import io.javaoperatorsdk.operator.processing.event.ResourceID;

import com.onyxdb.mongodbOperator.resources.ManagedMongoDB;
import com.onyxdb.mongodbOperator.resources.MongoStatefulSet;
import com.onyxdb.mongodbOperator.utils.K8sUtil;

/**
 * @author foxleren
 */
public class MongoStatefulSetDiscriminator extends ResourceIDMatcherDiscriminator<StatefulSet, ManagedMongoDB> {
    public MongoStatefulSetDiscriminator() {
        super(
                MongoStatefulSet.DEPENDENT_NAME,
                primary -> new ResourceID(
                        K8sUtil.getResourceInstanceNameWithPrefix(primary),
                        primary.getMetadata().getNamespace()
                )
        );
    }
}

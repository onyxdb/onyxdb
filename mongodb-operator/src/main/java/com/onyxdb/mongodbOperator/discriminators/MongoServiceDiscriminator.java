package com.onyxdb.mongodbOperator.discriminators;

import io.fabric8.kubernetes.api.model.Service;
import io.javaoperatorsdk.operator.api.reconciler.ResourceIDMatcherDiscriminator;
import io.javaoperatorsdk.operator.processing.event.ResourceID;

import com.onyxdb.mongodbOperator.resources.ManagedMongoDB;
import com.onyxdb.mongodbOperator.resources.MongoService;
import com.onyxdb.mongodbOperator.utils.K8sUtil;

/**
 * @author foxleren
 */
public class MongoServiceDiscriminator extends ResourceIDMatcherDiscriminator<Service, ManagedMongoDB> {
    public MongoServiceDiscriminator() {
        super(
                MongoService.DEPENDENT_NAME,
                primary -> new ResourceID(
                        K8sUtil.getResourceInstanceNameWithPrefix(primary),
                        primary.getMetadata().getNamespace()
                )
        );
    }
}

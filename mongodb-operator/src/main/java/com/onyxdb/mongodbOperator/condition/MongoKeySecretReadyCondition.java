package com.onyxdb.mongodbOperator.condition;

import io.fabric8.kubernetes.api.model.Secret;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.dependent.DependentResource;
import io.javaoperatorsdk.operator.processing.dependent.workflow.Condition;

import com.onyxdb.mongodbOperator.resources.ManagedMongoDB;

/**
 * @author foxleren
 */
public class MongoKeySecretReadyCondition implements Condition<Secret, ManagedMongoDB>  {
    @Override
    public boolean isMet(DependentResource<Secret, ManagedMongoDB> dependentResource,
                         ManagedMongoDB primary,
                         Context<ManagedMongoDB> context)
    {
        return dependentResource.getSecondaryResource(primary, context).isPresent();
    }
}

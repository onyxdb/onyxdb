package com.onyxdb.mongodbOperator.condition;

import java.util.Objects;

import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.dependent.DependentResource;
import io.javaoperatorsdk.operator.processing.dependent.workflow.Condition;

import com.onyxdb.mongodbOperator.resources.ManagedMongoDB;

/**
 * @author foxleren
 */
public class MongoStatefulSetReadyCondition implements Condition<StatefulSet, ManagedMongoDB> {
    @Override
    public boolean isMet(DependentResource<StatefulSet, ManagedMongoDB> dependentResource,
                         ManagedMongoDB primary,
                         Context<ManagedMongoDB> context)
    {
        return dependentResource.getSecondaryResource(primary, context)
                .map(statefulSet -> Objects.equals(
                        statefulSet.getSpec().getReplicas(),
                        statefulSet.getStatus().getReadyReplicas()
                ))
                .orElse(false);
    }
}

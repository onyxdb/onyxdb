package com.onyxdb.mongodbK8sOperator.controllers;

import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;
import org.springframework.stereotype.Component;

import com.onyxdb.mongodbK8sOperator.crds.ManagedMongoDbStatefulSetResource;
import com.onyxdb.mongodbK8sOperator.crds.ManagedMongoDbResource;
import com.onyxdb.mongodbK8sOperator.crds.ManagedMongoDbServiceResource;

/**
 * @author foxleren
 */
@Component
@ControllerConfiguration(dependents = {
        @Dependent(name = ManagedMongoDbStatefulSetResource.COMPONENT, type = ManagedMongoDbStatefulSetResource.class),
        @Dependent(name = ManagedMongoDbServiceResource.COMPONENT, type = ManagedMongoDbServiceResource.class),
})
public class MongoDBClusterReconciler implements Reconciler<ManagedMongoDbResource> {
    @Override
    public UpdateControl<ManagedMongoDbResource> reconcile(
            ManagedMongoDbResource resource,
            Context<ManagedMongoDbResource> context)
    {
        return UpdateControl.noUpdate();
    }
}

package com.onyxdb.mongodbOperator.controllers;

import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;
import org.springframework.stereotype.Component;

import com.onyxdb.mongodbOperator.crds.ManagedMongodbResource;
import com.onyxdb.mongodbOperator.crds.ManagedMongodbServiceResource;
import com.onyxdb.mongodbOperator.crds.ManagedMongodbStatefulSetResource;

/**
 * @author foxleren
 */
@Component
@ControllerConfiguration(dependents = {
        @Dependent(name = ManagedMongodbStatefulSetResource.COMPONENT, type = ManagedMongodbStatefulSetResource.class),
        @Dependent(name = ManagedMongodbServiceResource.COMPONENT, type = ManagedMongodbServiceResource.class),
})
public class ManagedMongodbReconciler implements Reconciler<ManagedMongodbResource> {
    @Override
    public UpdateControl<ManagedMongodbResource> reconcile(
            ManagedMongodbResource resource,
            Context<ManagedMongodbResource> context)
    {
        return UpdateControl.noUpdate();
    }
}

package com.onyxdb.mongodbOperator.controllers;

import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;
import org.springframework.stereotype.Component;

import com.onyxdb.mongodbOperator.resources.ManagedMongoDB;
import com.onyxdb.mongodbOperator.resources.ManagedMongoDBSecret;
import com.onyxdb.mongodbOperator.resources.ManagedMongodbService;
import com.onyxdb.mongodbOperator.resources.ManagedMongodbStatefulSet;

/**
 * @author foxleren
 */
@Component
@ControllerConfiguration(dependents = {
        @Dependent(name = ManagedMongoDBSecret.DEPENDENT_NAME, type = ManagedMongoDBSecret.class),
        @Dependent(name = ManagedMongodbService.DEPENDENT_NAME, type = ManagedMongodbService.class),
        @Dependent(name = ManagedMongodbStatefulSet.DEPENDENT_NAME, type = ManagedMongodbStatefulSet.class),
})
public class ManagedMongoDBReconciler implements Reconciler<ManagedMongoDB> {
    @Override
    public UpdateControl<ManagedMongoDB> reconcile(
            ManagedMongoDB resource,
            Context<ManagedMongoDB> context)
    {
        // TODO add logic and configure rs
        return UpdateControl.noUpdate();
    }
}

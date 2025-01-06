package com.onyxdb.mongodbOperator.reconcilers;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.reconciler.Cleaner;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.DeleteControl;
import io.javaoperatorsdk.operator.api.reconciler.ErrorStatusHandler;
import io.javaoperatorsdk.operator.api.reconciler.ErrorStatusUpdateControl;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;
import io.javaoperatorsdk.operator.processing.dependent.workflow.WorkflowReconcileResult;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.onyxdb.mongodbOperator.condition.MongoDBSecretReadyCondition;
import com.onyxdb.mongodbOperator.condition.MongoDBServiceReadyCondition;
import com.onyxdb.mongodbOperator.condition.MongoDBStatefulSetReadyCondition;
import com.onyxdb.mongodbOperator.resources.ManagedMongoDB;
import com.onyxdb.mongodbOperator.resources.MongoDBStatefulSet;
import com.onyxdb.mongodbOperator.resources.MongoDbSecret;
import com.onyxdb.mongodbOperator.resources.MongoDbService;
import com.onyxdb.mongodbOperator.resources.MongoDbState;
import com.onyxdb.mongodbOperator.resources.MongodbStatus;
import com.onyxdb.mongodbOperator.utils.LabelsUtil;
import com.onyxdb.mongodbOperator.utils.PodsUtil;

/**
 * @author foxleren
 */
@Component
@ControllerConfiguration(dependents = {
        @Dependent(
                name = MongoDbSecret.DEPENDENT_NAME,
                type = MongoDbSecret.class,
                readyPostcondition = MongoDBSecretReadyCondition.class
        ),
        @Dependent(
                name = MongoDbService.DEPENDENT_NAME,
                type = MongoDbService.class,
                readyPostcondition = MongoDBServiceReadyCondition.class
        ),
        @Dependent(
                name = MongoDBStatefulSet.DEPENDENT_NAME,
                type = MongoDBStatefulSet.class,
                dependsOn = {MongoDbSecret.DEPENDENT_NAME, MongoDbService.DEPENDENT_NAME},
                readyPostcondition = MongoDBStatefulSetReadyCondition.class
        ),
})
@RequiredArgsConstructor
public class MongoDBReconciler
        implements Reconciler<ManagedMongoDB>,
        ErrorStatusHandler<ManagedMongoDB>,
        Cleaner<ManagedMongoDB> {
    private static final Logger logger = LoggerFactory.getLogger(MongoDBReconciler.class);

    private static final Duration RECONCILE_SHORT_DELAY = Duration.ofSeconds(5);
//    private static final Duration RECONCILE_LONG_DELAY = Duration.ofHours(1);

    private final KubernetesClient kubernetesClient;

    @Override
    public UpdateControl<ManagedMongoDB> reconcile(ManagedMongoDB primaryResource, Context<ManagedMongoDB> context) {
        String primaryName = primaryResource.getMetadata().getName();

        logger.info("Reconciling ManagedMongoDB with name={}", primaryName);
        UpdateControl<ManagedMongoDB> updateControl = context.managedDependentResourceContext()
                .getWorkflowReconcileResult()
                .map(r -> handleWorkflowReconcileResult(primaryResource, context, r))
                .orElseThrow();
        logger.info("Reconciled ManagedMongoDB with name={}", primaryName);

        return updateControl;
    }

    @Override
    public ErrorStatusUpdateControl<ManagedMongoDB> updateErrorStatus(ManagedMongoDB resource, Context<ManagedMongoDB> context, Exception e) {
        resource.setStatus(MongodbStatus.withErrorState(e.getMessage()));
        return ErrorStatusUpdateControl.patchStatus(resource);
    }

    @Override
    public DeleteControl cleanup(ManagedMongoDB resource, Context<ManagedMongoDB> context) {
        return DeleteControl.defaultDelete();
    }

    private UpdateControl<ManagedMongoDB> handleWorkflowReconcileResult(
            ManagedMongoDB primaryResource,
            Context<ManagedMongoDB> context,
            WorkflowReconcileResult reconcileResult)
    {
        if (!reconcileResult.allDependentResourcesReady()) {
            primaryResource.setStatus(new MongodbStatus(MongoDbState.INITIALIZING, "Pending secondary resources"));
            return UpdateControl.patchStatus(primaryResource).rescheduleAfter(RECONCILE_SHORT_DELAY);
        }

        return reconcileMongoDBCluster(primaryResource, context);

//        String primaryNamespace = primary.getMetadata().getNamespace();
//        String primaryName = primary.getMetadata().getName();
//
//        String serviceName = context.getSecondaryResource(StatefulSet.class).orElseThrow().getSpec().getServiceName();
//
//        List<Pod> pods = kubernetesClient
//                .pods()
//                .inNamespace(primaryNamespace)
//                .withLabelSelector(new LabelSelectorBuilder()
//                        .withMatchLabels(LabelsUtil.getClusterLabels(primaryName))
//                        .build())
//                .list()
//                .getItems();
//
//        MongodbStatus mongodbStatus = null;
//        for (Pod pod : pods) {
//            pod.setStatus(new PodStatus());
//            if (!PodsUtil.isPodRunning(pod) || !PodsUtil.isPodContainerReady(pod, MongoDBStatefulSet.MONGODB_CONTAINER_NAME)) {
//                mongodbStatus = (new MongodbStatus(
//                        MongoDbState.INITIALIZING,
//                        String.format("Pod with MongoDB %s is not ready", pod.getMetadata().getName())
//                ));
//                logger.info("Pod or container aren't ready: " + pod.getMetadata().getName());
//            }
//        }

//        List<Pod>
//        AtomicReference<MongodbStatus> mongodbStatus = new AtomicReference<>();
//        mongodbStatus.getAndSet();
//        kubernetesClient.pods().inNamespace("onyxdb").withLabelSelector(
//                new LabelSelectorBuilder()
//                        .withMatchLabels(LabelsUtil.getClusterLabels(primary.getMetadata().getName()))
//                        .build()
//        ).list().getItems().forEach(pod -> {
//            if (!PodsUtil.isPodRunning(pod) || !PodsUtil.isPodContainerReady(pod, MongoDBStatefulSet.MONGODB_CONTAINER_NAME)) {
//                mongodbStatus.set(new MongodbStatus(
//                        MongoDbState.INITIALIZING,
//                        String.format("Pod with MongoDB %s is not ready", pod.getMetadata().getName())
//                ));
//                logger.info("Pod or container aren't ready: " + pod.getMetadata().getName());
//                return;
//            }
////            Pod p = r;
////            Phase
////            r.get().getStatus().getPhase()
//            var mongodbContainer = pod.getSpec().getContainers().stream().filter(rr -> Objects.equals(rr.getName(), "mongodb"))
//                    .findFirst().orElseThrow(() -> new RuntimeException("Can't find mongodb container in pod " + pod.getMetadata().getName()));
//            logger.info(mongodbContainer.getImage());
////            String[] command = {
////                    "mongosh",
////                    "--eval",
//////                    "sh", "-c",
////                    "\"rs.initiate(" +
////                            "{" +
////                            "_id: \"rs0\"," +
////                            " members: [" +
////                            " { _id: 0, host: \"managed-mongodb-test-0.managed-mongodb-test.onyxdb:27017\" }," +
////                            " { _id: 1, host: \"managed-mongodb-test-1.managed-mongodb-test.onyxdb:27017\" }," +
////                            " { _id: 2, host: \"managed-mongodb-test-2.managed-mongodb-test.onyxdb:27017\" }" +
////                            "]" +
////                            "}" + ")\""
////            };
////
////            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
////            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
////
////            // TODO exec with retries
////            try (ExecWatch execWatch = kubernetesClient.pods()
////                    .inNamespace("onyxdb")
////                    .withName(r.get().getMetadata().getName())
////                    .inContainer("mongodb") // Optional; remove if not targeting a specific container
////                    .writingOutput(outputStream)
////                    .writingError(errorStream)
////                    .exec(command)) {
////
////                // Wait for the command to complete
////                try {
////                    Thread.sleep(5000);
////                } catch (InterruptedException e) {
////                    throw new RuntimeException(e);
////                }
////                // Command Output: { ok: 1 }
////                // Command Error: MongoServerError: already initialized
////                System.out.println("Command Output: " + outputStream.toString());
////                System.err.println("Command Error: " + errorStream.toString());
////            }
//        });
//        mongodbStatus

//        primaryResource.setStatus(new MongodbStatus(MongoDbState.READY));
//        return UpdateControl.patchStatus(primaryResource).rescheduleAfter(RECONCILE_SHORT_DELAY);
    }

    private UpdateControl<ManagedMongoDB> reconcileMongoDBCluster(
            ManagedMongoDB primaryResource,
            Context<ManagedMongoDB> context)
    {
        String primaryName = primaryResource.getMetadata().getName();
        String primaryNamespace = primaryResource.getMetadata().getNamespace();

        List<Pod> pods = getStatefulSetPods(primaryName, primaryNamespace);

//        Optional<UpdateControl<ManagedMongoDB>> updateControlAfterHandledPodsO = handlePods(primaryResource, pods);
//        if (updateControlAfterHandledPodsO.isPresent()) {
//            return updateControlAfterHandledPodsO.get();
//        }

        var i = 0;

        return UpdateControl.noUpdate();
    }

//    private Optional<UpdateControl<ManagedMongoDB>> handlePods(ManagedMongoDB primaryResource, List<Pod> pods) {
//        for (Pod pod : pods) {
//            if (!PodsUtil.isPodRunning(pod) ||
//                    !PodsUtil.isPodContainerReady(pod, MongoDBStatefulSet.MONGODB_CONTAINER_NAME))
//            {
//                var status = new MongodbStatus(MongoDbState.INITIALIZING, "Waiting for the pods");
//                primaryResource.setStatus(status);
//                return Optional.of(UpdateControl.patchStatus(primaryResource).rescheduleAfter(RECONCILE_SHORT_DELAY));
//            }
//        }
//        return Optional.empty();
//    }

    private List<Pod> getStatefulSetPods(String primaryName, String primaryNamespace) {
        return kubernetesClient
                .pods()
                .inNamespace(primaryNamespace)
                .withLabelSelector(new LabelSelectorBuilder()
                        .withMatchLabels(LabelsUtil.getClusterLabels(primaryName))
                        .build())
                .list()
                .getItems();
    }
}

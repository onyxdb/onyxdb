package com.onyxdb.mongodbOperator.reconcilers;

import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
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

import com.onyxdb.mongodbOperator.condition.MongoSecretReadyCondition;
import com.onyxdb.mongodbOperator.condition.MongoServiceReadyCondition;
import com.onyxdb.mongodbOperator.condition.MongoStatefulSetReadyCondition;
import com.onyxdb.mongodbOperator.resources.ManagedMongoDB;
import com.onyxdb.mongodbOperator.resources.MongoSecret;
import com.onyxdb.mongodbOperator.resources.MongoService;
import com.onyxdb.mongodbOperator.resources.MongoStatefulSet;
import com.onyxdb.mongodbOperator.status.MongoState;
import com.onyxdb.mongodbOperator.status.MongoStatus;

/**
 * @author foxleren
 */
@Component
@ControllerConfiguration(dependents = {
        @Dependent(
                name = MongoSecret.DEPENDENT_NAME,
                type = MongoSecret.class,
                readyPostcondition = MongoSecretReadyCondition.class
        ),
        @Dependent(
                name = MongoService.DEPENDENT_NAME,
                type = MongoService.class,
                readyPostcondition = MongoServiceReadyCondition.class
        ),
        @Dependent(
                name = MongoStatefulSet.DEPENDENT_NAME,
                type = MongoStatefulSet.class,
                dependsOn = {MongoSecret.DEPENDENT_NAME, MongoService.DEPENDENT_NAME},
                readyPostcondition = MongoStatefulSetReadyCondition.class
        ),
})
@RequiredArgsConstructor
public class ManagedMongoDBReconciler
        implements Reconciler<ManagedMongoDB>,
        ErrorStatusHandler<ManagedMongoDB>,
        Cleaner<ManagedMongoDB>
{
    private static final Logger logger = LoggerFactory.getLogger(ManagedMongoDBReconciler.class);
    private static final Duration RECONCILE_DELAY = Duration.ofSeconds(5);

    private final KubernetesClient kubernetesClient;

    @Override
    public UpdateControl<ManagedMongoDB> reconcile(ManagedMongoDB primaryResource, Context<ManagedMongoDB> context) {
        String crdName = primaryResource.getCRDName();
        String primaryName = primaryResource.getMetadata().getName();

        logger.info("Reconciling {} with name={}", crdName, primaryName);
        UpdateControl<ManagedMongoDB> updateControl = context.managedDependentResourceContext()
                .getWorkflowReconcileResult()
                .map(r -> handleWorkflowReconcileResult(primaryResource, context, r))
                .orElseThrow();
        logger.info("Reconciled {} with name={}", crdName, primaryName);

        return updateControl;
    }

    @Override
    public ErrorStatusUpdateControl<ManagedMongoDB> updateErrorStatus(ManagedMongoDB resource, Context<ManagedMongoDB> context, Exception e) {
        resource.setStatus(MongoStatus.withErrorState(e.getMessage()));
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
            logger.info("Pending secondary resources for primary resource {}", primaryResource.getMetadata().getName());
            primaryResource.setStatus(new MongoStatus(MongoState.INITIALIZING, "Pending secondary resources"));
            return UpdateControl.patchStatus(primaryResource).rescheduleAfter(RECONCILE_DELAY);
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
//            String[] command = {
//                    "mongosh",
//                    "--eval",
////                    "sh", "-c",
//                    "\"rs.initiate(" +
//                            "{" +
//                            "_id: \"rs0\"," +
//                            " members: [" +
//                            " { _id: 0, host: \"managed-mongodb-test-0.managed-mongodb-test.onyxdb:27017\" }," +
//                            " { _id: 1, host: \"managed-mongodb-test-1.managed-mongodb-test.onyxdb:27017\" }," +
//                            " { _id: 2, host: \"managed-mongodb-test-2.managed-mongodb-test.onyxdb:27017\" }" +
//                            "]" +
//                            "}" + ")\""
//            };
////
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

////            // TODO exec with retries
//            try (ExecWatch execWatch = kubernetesClient.pods()
//                    .inNamespace("onyxdb")
//                    .withName(r.get().getMetadata().getName())
//                    .inContainer("mongodb") // Optional; remove if not targeting a specific container
//                    .writingOutput(outputStream)
//                    .writingError(errorStream)
//                    .exec(command)) {
//
//                // Wait for the command to complete
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                // Command Output: { ok: 1 }
//                // Command Error: MongoServerError: already initialized
//                System.out.println("Command Output: " + outputStream.toString());
//                System.err.println("Command Error: " + errorStream.toString());
//            }
//        });
//        mongodbStatus

//        primaryResource.setStatus(new MongodbStatus(MongoDbState.READY));
//        return UpdateControl.patchStatus(primaryResource).rescheduleAfter(RECONCILE_SHORT_DELAY);
    }

    private UpdateControl<ManagedMongoDB> reconcileMongoDBCluster(
            ManagedMongoDB primary,
            Context<ManagedMongoDB> context)
    {
        String primaryNamespace = primary.getMetadata().getNamespace();
        String primaryName = primary.getMetadata().getName();

        var kubernetesClient2 = context.getClient();

//        MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
//                .applyToClusterSettings(b -> b.hosts(List.of())).build());
//
//        mongoClient.close();

//        try {
//            StatefulSet statefulSet = context.getSecondaryResource(StatefulSet.class).orElseThrow();
//            List<Pod> pods = getStatefulSetPods(primaryNamespace, primaryName);
//            String serviceName = MongoService.getServiceName(primaryName);
//
//            initMongoReplicaSet(statefulSet, pods, serviceName, primaryNamespace);
//        } catch (Exception e) {
//            mongoClient.close();
//        }


//        Optional<UpdateControl<ManagedMongoDB>> updateControlAfterHandledPodsO = handlePods(primary, pods);
//        if (updateControlAfterHandledPodsO.isPresent()) {
//            return updateControlAfterHandledPodsO.get();
//        }

//        var i = 0;

        return UpdateControl.patchStatus(primary).rescheduleAfter(RECONCILE_DELAY);
    }

    private void initMongoReplicaSet(StatefulSet statefulSet, List<Pod> pods, String serviceName, String namespace) {
        StringBuilder membersStringBuilder = new StringBuilder();
        logger.info("" + pods.size());
        for (int i = 0; i < pods.size(); i++) {
            String member = String.format("{ _id: %d, host: \"%s.%s.%s:27017\" }", i, pods.get(i).getMetadata().getName(), serviceName, namespace);
            membersStringBuilder.append(member);
            if (i < pods.size() - 1) {
                membersStringBuilder.append(",");
            }
            membersStringBuilder.append("\n");
        }
//        logger.info(membersStringBuilder.toString());

        String[] command = {
                "mongosh",
                "--eval",
                String.format("""
                        rs.initiate(
                            {
                                _id: "rs0",
                                members: [
                                    %s
                                ]
                            }
                        )
                        """, membersStringBuilder)
        };
        logger.info(Arrays.toString(command));
        var outputStream = new ByteArrayOutputStream();
        var errorStream = new ByteArrayOutputStream();

        try (ExecWatch execWatch = kubernetesClient.pods()
                .inNamespace(namespace)
                .withName(pods.getFirst().getMetadata().getName())
                .inContainer(MongoStatefulSet.MONGODB_CONTAINER_NAME) // Optional; remove if not targeting a specific container
                .writingOutput(outputStream)
                .writingError(errorStream)
                .exec(command)) {

            // Wait for the command to complete
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            execWatch.
            // Command Output: { ok: 1 }
            // Command Error: MongoServerError: already initialized
            System.out.println("Command Output: " + outputStream.size());
            System.err.println("Command Error: " + errorStream.size());
            var r = execWatch.exitCode().join();
            logger.info("code: "+ r);
//            r.complete()
        }

    }
}

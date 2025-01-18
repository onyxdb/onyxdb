package com.onyxdb.mongodbOperator.reconcilers;

import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.List;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;
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

import com.onyxdb.mongodbOperator.condition.MongoKeySecretReadyCondition;
import com.onyxdb.mongodbOperator.condition.MongoSecretReadyCondition;
import com.onyxdb.mongodbOperator.condition.MongoServiceReadyCondition;
import com.onyxdb.mongodbOperator.condition.MongoStatefulSetReadyCondition;
import com.onyxdb.mongodbOperator.resources.ManagedMongoDB;
import com.onyxdb.mongodbOperator.resources.MongoKeySecret;
import com.onyxdb.mongodbOperator.resources.MongoSecret;
import com.onyxdb.mongodbOperator.resources.MongoService;
import com.onyxdb.mongodbOperator.resources.MongoStatefulSet;
import com.onyxdb.mongodbOperator.resources.MongoState;
import com.onyxdb.mongodbOperator.resources.MongoStatus;
import com.onyxdb.mongodbOperator.utils.K8sUtil;
import com.onyxdb.mongodbOperator.utils.LabelsUtils;
import com.onyxdb.mongodbOperator.utils.MetaUtils;
import com.onyxdb.mongodbOperator.utils.MongoUtil;

/**
 * @author foxleren
 */
@Component
@ControllerConfiguration(
//        namespaces = {"onyxdb"},
        dependents = {
//        @Dependent(
//                name = MongoSecret.DEPENDENT_NAME,
//                type = MongoSecret.class,
//                readyPostcondition = MongoSecretReadyCondition.class
//        ),
        @Dependent(
                name = MongoKeySecret.DEPENDENT_NAME,
                type = MongoKeySecret.class,
                readyPostcondition = MongoKeySecretReadyCondition.class
        ),
        @Dependent(
                name = MongoService.DEPENDENT_NAME,
                type = MongoService.class,
                readyPostcondition = MongoServiceReadyCondition.class
        ),
        @Dependent(
                name = MongoStatefulSet.DEPENDENT_NAME,
                type = MongoStatefulSet.class,
                dependsOn = {MongoKeySecret.DEPENDENT_NAME, MongoService.DEPENDENT_NAME},
                readyPostcondition = MongoStatefulSetReadyCondition.class
        ),
})
@RequiredArgsConstructor
public class ManagedMongoDBReconciler
        implements Reconciler<ManagedMongoDB>,
        ErrorStatusHandler<ManagedMongoDB>,
        Cleaner<ManagedMongoDB> {
    private static final Logger logger = LoggerFactory.getLogger(ManagedMongoDBReconciler.class);
    private static final Duration RECONCILE_DELAY = Duration.ofSeconds(5);

    private final KubernetesClient kubernetesClient;

    // TODO init mongod with --auth, then create users with client
    // https://www.mongodb.com/docs/manual/reference/program/mongod/#std-option-mongod.--auth

    // TODO create secret with generated keyfile for auth
    // https://www.mongodb.com/docs/manual/tutorial/deploy-replica-set-with-keyfile-access-control/
    // https://www.mongodb.com/docs/manual/core/security-internal-authentication/#keyfiles
    // https://www.digitalocean.com/community/tutorials/how-to-configure-keyfile-authentication-for-mongodb-replica-sets-on-ubuntu-20-04
    // https://stackoverflow.com/questions/53296057/how-do-i-mount-a-single-file-from-a-secret-in-kubernetes
    @Override
    public UpdateControl<ManagedMongoDB> reconcile(ManagedMongoDB primary, Context<ManagedMongoDB> context) {
        String crdName = primary.getCRDName();
        String primaryName = primary.getMetadata().getName();

        logger.info("MOKHOV_V=4");

        try {
            logger.info("Reconciling {} with name={}", crdName, primaryName);
            UpdateControl<ManagedMongoDB> updateControl = context.managedDependentResourceContext()
                    .getWorkflowReconcileResult()
                    .map(r -> handleWorkflowReconcileResult(primary, context, r))
                    .orElseThrow();
            logger.info("Reconciled {} with name={}", crdName, primaryName);
            return updateControl;
        } catch (Exception e) {
            logger.error("Failed to reconcile {} with name={}", crdName, primaryName, e);
            primary.setStatus(MongoStatus.withErrorState(e.getMessage()));
            return UpdateControl.patchStatus(primary).rescheduleAfter(RECONCILE_DELAY);
        }
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
            WorkflowReconcileResult reconcileResult) {
        if (!reconcileResult.allDependentResourcesReady()) {
            List<String> notReady = reconcileResult.getNotReadyDependents().stream().map(r -> r.resourceType().getName()).toList();
            logger.info("Pending secondary resources for primary resource {}: {}", primaryResource.getMetadata().getName(),
                    String.join(", ", notReady));
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
            Context<ManagedMongoDB> context) {
        String primaryNamespace = primary.getMetadata().getNamespace();
        String primaryName = primary.getMetadata().getName();

        StatefulSet statefulSet = context.getSecondaryResource(StatefulSet.class).orElseThrow();
        List<Pod> pods = getStatefulSetPods(primaryNamespace, primaryName);
        String serviceName = MetaUtils.getResourceInstanceNameWithPrefix(primary);

//        logger.info("KEY=" + K8sUtil.generateBase64Key(1024));

//        var mongoUrl = "mongodb://user:password@managed-mongodb-sample-db-0.managed-mongodb-sample-db.onyxdb:27017,managed-mongodb-sample-db-1.managed-mongodb-sample-db.onyxdb:27017,managed-mongodb-sample-db-2.managed-mongodb-sample-db.onyxdb:27017/admin?replicaSet=rs0";
//        try(MongoClient mongoClient = MongoClients.create(mongoUrl)) {
//            var desc = mongoClient.getClusterDescription();
//            logger.info(desc.getShortDescription());
//            for (var a : mongoClient.listDatabaseNames()) {
//                logger.info("db=" + a);
//            }
//        }
        // TODO if cant init mongo rs, then set error state
//        initMongoReplicaSet(statefulSet, pods, serviceName, primaryNamespace);

        primary.setStatus(new MongoStatus(MongoState.READY));
        return UpdateControl.patchStatus(primary).rescheduleAfter(RECONCILE_DELAY);
    }

    private void initMongoReplicaSet(StatefulSet statefulSet, List<Pod> pods, String serviceName, String namespace) {
        List<String> hosts = pods.stream()
                .map(p -> K8sUtil.getPodFqdn(p.getMetadata().getName(), serviceName, namespace))
                .toList();
        String initRsCmd = MongoUtil.buildInitReplicaSetCommand("rs0", hosts, MongoStatefulSet.MONGODB_CONTAINER_PORT);
        logger.info(initRsCmd);

        String[] command = {"mongosh", "-u user -p password ", "--eval", initRsCmd};

        var outputStream = new ByteArrayOutputStream();
        var errorStream = new ByteArrayOutputStream();

        ExecWatch execWatch = kubernetesClient.pods()
                .inNamespace(namespace)
                .withName(pods.getFirst().getMetadata().getName())
                .inContainer(MongoStatefulSet.MONGODB_CONTAINER_NAME) // Optional; remove if not targeting a specific container
                .writingOutput(outputStream)
                .writingError(errorStream)
                .exec(command);
        try (execWatch) {
            int exitCode = execWatch.exitCode().join();
            if (exitCode != 0) {
                throw new RuntimeException(String.format(
                        "Failed to init mongo ReplicaSet; exitCode=%d; message=%s",
                        exitCode,
                        errorStream
                ));
            }
        }
    }

    private List<Pod> getStatefulSetPods(String primaryNamespace, String primaryName) {
        return kubernetesClient
                .pods()
                .inNamespace(primaryNamespace)
                .withLabelSelector(new LabelSelectorBuilder()
                        .withMatchLabels(LabelsUtils.getClusterLabels(primaryName))
                        .build())
                .list()
                .getItems();
    }
}

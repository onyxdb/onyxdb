package com.onyxdb.mongodbOperator.reconcilers;

import java.io.ByteArrayOutputStream;
import java.time.Duration;

import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;
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

import com.onyxdb.mongodbOperator.resources.ManagedMongoDB;
import com.onyxdb.mongodbOperator.resources.MongoDbSecret;
import com.onyxdb.mongodbOperator.resources.MongoDbService;
import com.onyxdb.mongodbOperator.resources.MongoDbState;
import com.onyxdb.mongodbOperator.resources.MongoDbStatefulSet;
import com.onyxdb.mongodbOperator.resources.MongodbStatus;
import com.onyxdb.mongodbOperator.utils.LabelsUtil;

/**
 * @author foxleren
 */
@Component
@ControllerConfiguration(dependents = {
        @Dependent(name = MongoDbSecret.DEPENDENT_NAME, type = MongoDbSecret.class),
        @Dependent(
                name = MongoDbService.DEPENDENT_NAME,
                type = MongoDbService.class
        ),
        @Dependent(
                name = MongoDbStatefulSet.DEPENDENT_NAME,
                type = MongoDbStatefulSet.class,
                dependsOn = {MongoDbSecret.DEPENDENT_NAME, MongoDbService.DEPENDENT_NAME}
        ),
})
@RequiredArgsConstructor
public class MongoDBReconciler
        implements Reconciler<ManagedMongoDB>,
        ErrorStatusHandler<ManagedMongoDB>,
        Cleaner<ManagedMongoDB> {
    private static final Logger logger = LoggerFactory.getLogger(MongoDBReconciler.class);

    private static final Duration RECONCILE_SHORT_DELAY = Duration.ofSeconds(5);
    private static final Duration RECONCILE_LONG_DELAY = Duration.ofHours(1);

    private final KubernetesClient kubernetesClient;

    // TODO check created resources if they must be recreated.
    // TODO create mongodb users from secrets. Don't provide creds in manifest.
    // TODO lean about resource discriminators.
    // TODO what is TTY?
    @Override
    public UpdateControl<ManagedMongoDB> reconcile(ManagedMongoDB resource, Context<ManagedMongoDB> context) {
        String resourceMetaName = resource.getMetadata().getName();

//        logger.info("Reconciling ManagedMongoDB with name={}", resourceMetaName);

        UpdateControl<ManagedMongoDB> updateControl = context.managedDependentResourceContext()
                .getWorkflowReconcileResult()
                .map(r -> handleWorkflowReconcileResult(resource, context, r))
                .orElseThrow();

//        logger.info("Reconciled ManagedMongoDB with name={}", resourceMetaName);

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
            ManagedMongoDB primary,
            Context<ManagedMongoDB> context,
            WorkflowReconcileResult reconcileResult) {
        if (!reconcileResult.allDependentResourcesReady()) {
            primary.setStatus(new MongodbStatus(MongoDbState.INITIALIZING, "Pending secondary resources"));
            return UpdateControl.patchStatus(primary).rescheduleAfter(RECONCILE_SHORT_DELAY);
        }

        kubernetesClient.pods().inNamespace("onyxdb").withLabelSelector(
                new LabelSelectorBuilder()
                        .withMatchLabels(LabelsUtil.getClusterLabels(primary.getMetadata().getName()))
                        .build()
        ).resources().forEach(r -> {
            String[] command = {
                    "mongosh",
                    "--eval",
//                    "sh", "-c",
//                    """
////"rs.initiate(
//// {
////  _id: "rs0",
////  members: [
////   { _id: 0, host: "managed-mongodb-test-0.managed-mongodb-test.onyxdb:27017" },
////   { _id: 1, host: "managed-mongodb-test-1.managed-mongodb-test.onyxdb:27017" },
////   { _id: 2, host: "managed-mongodb-test-2.managed-mongodb-test.onyxdb:27017" }
////  ]
//// }
////)"
//""",
                    "\"rs.initiate(" +
                     "{" +
                      "_id: \"rs0\"," +
                     " members: [" +
                      " { _id: 0, host: \"managed-mongodb-test-0.managed-mongodb-test.onyxdb:27017\" }," +
                      " { _id: 1, host: \"managed-mongodb-test-1.managed-mongodb-test.onyxdb:27017\" }," +
                      " { _id: 2, host: \"managed-mongodb-test-2.managed-mongodb-test.onyxdb:27017\" }" +
                      "]"+
                     "}" + ")\""
            };

//                    "rs.initiate(" +
//                    " {\n" +
//                    "  _id: \"rs0\",\n" +
//                    "  members: [\n" +
//                    "   { _id: 0, host: \"managed-mongodb-0.managed-mongodb.onyxdb:27017\" },\n" +
//                    "   { _id: 1, host: \"managed-mongodb-1.managed-mongodb.onyxdb:27017\" },\n" +
//                    "   { _id: 2, host: \"managed-mongodb-2.managed-mongodb.onyxdb:27017\" }\n" +
//                    "  ]\n" +
//                    " }\n" +
//                    ")"
//                    }; // Replace with your command

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

            try (ExecWatch execWatch = kubernetesClient.pods()
                    .inNamespace("onyxdb")
                    .withName(r.get().getMetadata().getName())
                    .inContainer("mongodb") // Optional; remove if not targeting a specific container
                    .writingOutput(outputStream)
                    .writingError(errorStream)
//                    .withTTY()
                    .exec(command)) {

                // Wait for the command to complete
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
//                var ewe = execWatch.getError();

                // Print output and error
                System.out.println("Command Output: " + outputStream.toString());
                System.err.println("Command Error: " + errorStream.toString());
//                System.err.println("ewe: " + ewe.toString());
            }
//            try (
//                    KubernetesClient client = new KubernetesClientBuilder().build();
//                    ExecWatch watch = newExecWatch(client, "onyxdb", r.get().getMetadata().getName())) {
////                try {
////                    String text = new String(watch.getOutput().readAllBytes(), StandardCharsets.UTF_8);
////                    logger.info(text);
////                } catch (IOException e) {
////                    throw new RuntimeException(e);
////                }
//                watch.exitCode().join();
//            }
//            var output = r
//                    .writingOutput(System.out)
//                    .writingError(System.err)
//                    .withTTY()
//                    .usingListener(new SimpleListener())
//                    .exec("pwd");
//            try {
//                String text = new String(output.getOutput().readAllBytes(), StandardCharsets.UTF_8);
//                logger.info(text);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            output.getOutput().;
//            logger.info(r.get().getMetadata().getName());
        });

//        kubernetesClient.pods().

            primary.setStatus(new MongodbStatus(MongoDbState.READY));
            return UpdateControl.patchStatus(primary).rescheduleAfter(RECONCILE_SHORT_DELAY);
//        }
//        });
    }

//    private static ExecWatch newExecWatch(KubernetesClient client, String namespace, String podName) {
//        return client.pods().inNamespace(namespace).withName(podName)
////                .withPrettyOutput().getLog()
////                .writingError(System.err)
////                .withTTY()
////                .usingListener(new SimpleListener())
//                .exec("mongosh", "--version").getOutput().;
//    }

//    private void initMongoReplicaSet() {
//
//    }
//
//    private static class SimpleListener implements ExecListener {
//
//        @Override
//        public void onOpen() {
//            System.out.println("The shell will remain open for 10 seconds.");
//        }
//
//        @Override
//        public void onFailure(Throwable t, ExecListener.Response failureResponse) {
//            System.err.println("shell barfed");
//        }
//
//        @Override
//        public void onClose(int code, String reason) {
//            System.out.println("The shell will now close.");
//        }
//    }
}

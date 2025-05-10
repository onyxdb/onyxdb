package com.onyxdb.platform.mdb.clients.k8s.psmdb;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.ResourceDefinitionContext;
import org.jetbrains.annotations.Nullable;

import com.onyxdb.platform.mdb.backups.Backup;
import com.onyxdb.platform.mdb.backups.BackupStatus;
import com.onyxdb.platform.mdb.backups.BackupType;
import com.onyxdb.platform.mdb.clients.k8s.victoriaLogs.VictoriaLogsClient;
import com.onyxdb.platform.mdb.utils.ObjectMapperUtils;
import com.onyxdb.platform.mdb.utils.TemplateProvider;

// TODO rewrite all
public class PsmdbClient extends AbstractPsmdbFactory {
    private static final String GROUP = "psmdb.percona.com";
    private static final String VERSION = "v1";
    private static final String API_VERSION = GROUP + "/" + VERSION;
    private static final String KIND = "PerconaServerMongoDB";
    private static final ResourceDefinitionContext CONTEXT = new ResourceDefinitionContext.Builder()
            .withGroup(GROUP)
            .withVersion(VERSION)
            .withKind(KIND)
            .withNamespaced(true)
            .build();
    private static final ResourceDefinitionContext PSMDB_BACKUP_CONTEXT = new ResourceDefinitionContext.Builder()
            .withGroup(GROUP)
            .withVersion(VERSION)
            .withKind("PerconaServerMongoDBBackup")
            .withNamespaced(true)
            .build();
    private static final DateTimeFormatter BACKUP_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final KubernetesClient kubernetesClient;
    private final TemplateProvider templateProvider;
    private final VictoriaLogsClient victoriaLogsClient;
    private final ObjectMapper yamlObjectMapper;

    private static final String REPLSET_NAME = "rs0";

    public PsmdbClient(
            ObjectMapper objectMapper,
            KubernetesClient kubernetesClient,
            TemplateProvider templateProvider,
            VictoriaLogsClient victoriaLogsClient,
            ObjectMapper yamlObjectMapper
    ) {
        super(objectMapper);
        this.kubernetesClient = kubernetesClient;
        this.templateProvider = templateProvider;
        this.victoriaLogsClient = victoriaLogsClient;
        this.yamlObjectMapper = yamlObjectMapper;
    }

    @Nullable
    public GenericKubernetesResource getResource(String namespace, String project, String cluster) {
        return kubernetesClient.genericKubernetesResources(CONTEXT)
                .inNamespace(namespace)
                .withName(getPsmdbName(project, cluster))
                .get();
    }

    public void applyPsmdbCr(
            String namespace,
            String project,
            String cluster,
            int replsetSize,
            double vcpu,
            long ram,
            String storageClass,
            long storage,
            boolean backupEnabled,
            String backupSchedule,
            int backupLimit,
            String minioUrl,
            String minioSecret,
            String minioBucket
    ) {
        String resource = templateProvider.buildPsmdbCr(
                getPsmdbName(project, cluster),
                project,
                cluster,
                getSecretName(project, cluster),
                REPLSET_NAME,
                replsetSize,
                vcpu,
                ram,
                storageClass,
                storage,
                backupEnabled,
                backupSchedule,
                backupLimit,
                minioUrl,
                minioSecret,
                minioBucket
        );

        kubernetesClient.resource(resource)
                .inNamespace(namespace)
                .serverSideApply();
    }

    public void deletePsmdb(String namespace, String project, String cluster) {
        kubernetesClient.genericKubernetesResources(CONTEXT)
                .inNamespace(namespace)
                .withName(getPsmdbName(project, cluster))
                .delete();
    }

    // TODO get namespace from project data
    public boolean psmdbExists(String namespace, String project, String cluster) {
        return getResource(namespace, project, cluster) != null;
    }

    public boolean isResourceReady(String namespace, String project, String name) {
        GenericKubernetesResource resource = getResource(namespace, project, name);
        if (resource == null) {
            return false;
        }

        return getStateO(resource)
                .stream()
                .anyMatch(state -> state.equals(STATE_READY_VALUE));
    }

    public void createVectorConfig(
            String namespace,
            String project,
            String cluster
    ) {
        String resource = templateProvider.buildMongoVectorConfigMapManifest(
                getVectorConfigMapName(project, cluster),
                project,
                cluster,
                victoriaLogsClient.getBaseUrl()
        );
        kubernetesClient.resource(resource)
                .inNamespace(namespace)
                .serverSideApply();
    }

    public void deleteVectorConfig(
            String namespace,
            String project,
            String cluster
    ) {
        String resource = templateProvider.buildMongoVectorConfigMapManifest(
                getVectorConfigMapName(project, cluster),
                project,
                cluster,
                victoriaLogsClient.getBaseUrl()
        );
        kubernetesClient.resource(resource)
                .inNamespace(namespace)
                .delete();
    }

    public List<String> getPsmdbPods(String namespace, String project, String cluster) {
        return kubernetesClient.pods()
                .inNamespace(namespace)
                .withLabels(Map.ofEntries(
                        Map.entry("app.kubernetes.io/instance", getPsmdbName(project, cluster))
                ))
                .list()
                .getItems()
                .stream()
                .map(p -> p.getMetadata().getName())
                .toList();
    }

    public static List<String> calculatePsmdbHostNames(String project, String cluster, int replicas) {
        List<String> hosts = new ArrayList<>(replicas);
        for (int i = 0; i < replicas; i++) {
            hosts.add(String.format("%s-%s-mongo-%s-%d", cluster, project, REPLSET_NAME, i));
        }

        return hosts;
    }

    public String applyMongoUserSecret(
            String namespace,
            String project,
            String cluster,
            String user,
            String password
    ) {
        String secretName = getMongoUserSecretName(project, cluster, user);
        Secret secret = new SecretBuilder()
                .withNewMetadata()
                .withName(secretName)
                .withLabels(Map.ofEntries(
                        Map.entry("app.kubernetes.io/instance", getPsmdbName(project, cluster)))
                )
                .endMetadata()
                .addToStringData("userName", user)
                .addToStringData("password", password)
                .build();

        kubernetesClient.secrets()
                .inNamespace(namespace)
                .resource(secret)
                .serverSideApply();

        return secretName;
    }

    public void deleteAllSecrets(
            String namespace,
            String project,
            String cluster
    ) {
        kubernetesClient.secrets()
                .inNamespace(namespace)
                .withLabels((Map.ofEntries(
                        Map.entry("app.kubernetes.io/instance", getPsmdbName(project, cluster)))
                ))
                .delete();
    }

    public List<Backup> listBackups(
            String namespace,
            String project,
            String cluster
    ) {
        return kubernetesClient.genericKubernetesResources(PSMDB_BACKUP_CONTEXT)
                .inNamespace(namespace)
                .withLabels(Map.ofEntries(
                        Map.entry("app.kubernetes.io/instance", getPsmdbName(project, cluster))
                ))
                .list()
                .getItems().stream().map(item -> {
                    BackupType type = Optional.ofNullable(item.getMetadata().getLabels().get("percona.com/backup-type"))
                            .map(rawType -> {
                                if (rawType.equals("cron")) {
                                    return BackupType.AUTOMATED;
                                }
                                return BackupType.MANUAL;
                            })
                            .orElse(BackupType.MANUAL);

                    Object statusObj = item.getAdditionalProperties().get("status");
                    BackupStatus status = parsePsmdbBackupStatusFromGenericResource(item);
                    @Nullable
                    LocalDateTime startedAt = null;
                    @Nullable
                    LocalDateTime finishedAt = null;
                    if (statusObj != null) {
                        Map<String, Object> statusMap = ObjectMapperUtils.convertToMap(objectMapper, statusObj);
                        startedAt = Optional.ofNullable(objectMapper.convertValue(statusMap.get("start"), String.class))
                                .map(ZonedDateTime::parse).map(ZonedDateTime::toLocalDateTime).orElse(null);
                        finishedAt = Optional.ofNullable(objectMapper.convertValue(statusMap.get("completed"), String.class))
                                .map(ZonedDateTime::parse).map(ZonedDateTime::toLocalDateTime).orElse(null);
                    }
                    return new Backup(
                            item.getMetadata().getName(),
                            type,
                            status,
                            startedAt,
                            finishedAt
                    );
                })
                .sorted(Comparator.comparing(Backup::startedAt, Comparator.nullsLast(Comparator.naturalOrder()))
                        .reversed()
                )
                .toList();
    }

    public void applyPsmdbBackup(
            String namespace,
            String projectName,
            String clusterName,
            LocalDateTime createdAt
    ) {
        String resource = templateProvider.buildPsmdbBackup(
                getManualPsmdbBackupName(projectName, clusterName, createdAt),
                getPsmdbName(projectName, clusterName)
        );
        kubernetesClient.resource(resource)
                .inNamespace(namespace)
                .serverSideApply();
    }

    public boolean isPsmdbBackupReady(
            String namespace,
            String projectName,
            String clusterName,
            LocalDateTime createdAt
    ) {
        GenericKubernetesResource resource = kubernetesClient.genericKubernetesResources(PSMDB_BACKUP_CONTEXT)
                .inNamespace(namespace)
                .withName(getManualPsmdbBackupName(projectName, clusterName, createdAt))
                .get();

        return parsePsmdbBackupStatusFromGenericResource(resource).equalsStringEnum(BackupStatus.READY);
    }

    public static String getPsmdbName(String project, String cluster) {
        return String.format("%s-%s-mongo", cluster, project);
    }

    public static String getPsmdbRsServiceName(String project, String cluster) {
        return String.format("%s-%s-mongo-rs0", cluster, project);
    }

    public static String getSecretName(String project, String cluster) {
        return String.format("%s-%s-mongo-users", cluster, project);
    }

    public static String getVectorConfigMapName(String project, String cluster) {
        return String.format("%s-%s-mongo-vector", cluster, project);
    }

    public static String getMongoUserSecretName(String project, String cluster, String user) {
        return String.format("%s-%s-mongo-user-%s", cluster, project, user);
    }

    public static String getManualPsmdbBackupName(
            String project,
            String cluster,
            LocalDateTime createdAt
    ) {
        return "manual-%s-%s".formatted(getPsmdbName(project, cluster), createdAt.format(BACKUP_DATE_TIME_FORMATTER));
    }

    private BackupStatus parsePsmdbBackupStatusFromGenericResource(GenericKubernetesResource resource) {
        Object statusObj = resource.getAdditionalProperties().get("status");

        BackupStatus status = BackupStatus.UNKNOWN;
        if (statusObj != null) {
            Map<String, Object> statusMap = ObjectMapperUtils.convertToMap(objectMapper, statusObj);

            @Nullable
            Object stateObj = statusMap.get("state");
            if (stateObj != null) {
                String rawState = objectMapper.convertValue(stateObj, String.class);
                if (rawState.equals("ready")) {
                    status = BackupStatus.READY;
                } else if (rawState.equals("running")) {
                    status = BackupStatus.RUNNING;
                }
            }

        }

        return status;
    }

//    public static List<String> getPsmdbPods(String cluster, ) {
//
//    }

//    private GenericKubernetesResource buildResource(Psmdb psmdb) {
//        var specMap = ObjectMapperUtils.convertToMap(objectMapper, psmdb.spec());
//
//        return new GenericKubernetesResourceBuilder()
//                .withApiVersion(API_VERSION)
//                .withKind(KIND)
//                .withNewMetadata()
//                .withName(getPsmdbName(psmdb.database()))
//                .endMetadata()
//                .addToAdditionalProperties("spec", specMap)
//                .build();
//    }
}

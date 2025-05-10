package com.onyxdb.platform.mdb.utils;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import io.fabric8.kubernetes.api.model.Quantity;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.onyxdb.platform.mdb.context.OnyxdbInitializer;

import static com.onyxdb.platform.mdb.clusters.ClusterMapper.DEFAULT_NAMESPACE;

public class TemplateProvider {
    private final SpringTemplateEngine templateEngine;

    public TemplateProvider(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String buildMongoVectorConfigMapManifest(
            String metadataName,
            String project,
            String cluster,
            String vlogsBaseUrl
    ) {
        Context context = new Context();
        context.setVariables(Map.ofEntries(
                Map.entry("METADATA_NAME", metadataName),
                Map.entry("ONYXDB_PROJECT", project),
                Map.entry("ONYXDB_CLUSTER", cluster),
                Map.entry("VLOGS_BASE_URL", vlogsBaseUrl)
        ));

        return templateEngine.process("mongo-vector-config-map.yaml.txt", context);
    }

    public String buildPsmdbCr(
            String metadataName,
            String project,
            String cluster,
            String secretsUsersName,
            String replsetName,
            int replsetSize,
            long vcpuMillis,
            long ramBytes,
            String storageClass,
            long storageBytes,
            boolean backupEnabled,
            String backupSchedule,
            int backupLimit,
            String minioUrl,
            String minioSecret,
            String minioBucket
    ) {
        var mongodMemory = Quantity.fromNumericalAmount(BigDecimal.valueOf(ramBytes), "Gi");
        var mongodStorage = Quantity.fromNumericalAmount(BigDecimal.valueOf(storageBytes), "Gi");

        Context context = new Context();
        context.setVariables(Map.ofEntries(
                Map.entry("METADATA_NAME", metadataName),
                Map.entry("ONYXDB_PROJECT", project),
                Map.entry("ONYXDB_CLUSTER", cluster),
                Map.entry("SECRETS_USERS_NAME", secretsUsersName),
                Map.entry("REPLSET_NAME", replsetName),
                Map.entry("REPLSET_SIZE", replsetSize),
                Map.entry("MONGOD_CPU", vcpuMillis + "m"),
                Map.entry("MONGOD_MEMORY", mongodMemory),
                Map.entry("MONGOD_STORAGE_CLASS", storageClass),
                Map.entry("MONGOD_STORAGE", mongodStorage),
                Map.entry("BACKUP_ENABLED", backupEnabled),
                Map.entry("MINIO_BUCKET", minioBucket),
                Map.entry("MINIO_SECRET", minioSecret),
                Map.entry("MINIO_URL", minioUrl),
                Map.entry("BACKUP_SCHEDULE", backupSchedule),
                Map.entry("BACKUP_LIMIT", backupLimit)
        ));

        return templateEngine.process("psmdb.yaml.txt", context);
    }

    public String buildOnyxdbAgent(
            String metadataName,
            String onyxdbBaseUrl,
            UUID clusterId,
            String secretsUsersName,
            String rsServiceName
    ) {
        Context context = new Context();
        context.setVariables(Map.ofEntries(
                Map.entry("METADATA_NAME", metadataName),
                Map.entry("ONYXDB_BASE_URL", onyxdbBaseUrl),
                Map.entry("ONYXDB_CLUSTER_ID", clusterId),
                Map.entry("ONYXDB_ROBOT_SECRET", OnyxdbInitializer.ONYXDB_ROBOT_SECRET),
                Map.entry("SECRETS_USERS_NAME", secretsUsersName),
                Map.entry("REPLSET_SERVICE", rsServiceName),
                Map.entry("NAMESPACE", DEFAULT_NAMESPACE)
        ));

        return templateEngine.process("onyxdb-agent.yaml.txt", context);
    }

    public String buildPsmdbExporterService(
            String metadataName,
            String portName,
            int portNumber,
            String selectorInstanceLabel
    ) {
        Context context = new Context();
        context.setVariables(Map.ofEntries(
                Map.entry("METADATA_NAME", metadataName),
                Map.entry("SELECTOR_INSTANCE_LABEL", selectorInstanceLabel),
                Map.entry("PORT_NAME", portName),
                Map.entry("PORT_NUMBER", portNumber)
        ));

        return templateEngine.process("psmdb-exporter-service.yaml.txt", context);
    }

    public String buildPsmdbExporterServiceScrape(
            String metadataName,
            String endpointPortName,
            String endpointPath,
            String project,
            String cluster,
            String selectorInstanceLabel
    ) {
        Context context = new Context();
        context.setVariables(Map.ofEntries(
                Map.entry("METADATA_NAME", metadataName),
                Map.entry("ENDPOINT_PORT_NAME", endpointPortName),
                Map.entry("ENDPOINT_PATH", endpointPath),
                Map.entry("ONYXDB_PROJECT", project),
                Map.entry("ONYXDB_CLUSTER", cluster),
                Map.entry("SELECTOR_INSTANCE_LABEL", selectorInstanceLabel)
        ));

        return templateEngine.process("psmdb-exporter-service-scrape.yaml.txt", context);
    }

    public String buildPsmdbBackup(String metadataName, String clusterName) {
        Context context = new Context();
        context.setVariables(Map.ofEntries(
                Map.entry("METADATA_NAME", metadataName),
                Map.entry("CLUSTER_NAME", clusterName)
        ));

        return templateEngine.process("psmdb-backup.yaml.txt", context);
    }

    public String buildPsmdbRestore(String metadataName, String clusterName, String backupName) {
        Context context = new Context();
        context.setVariables(Map.ofEntries(
                Map.entry("METADATA_NAME", metadataName),
                Map.entry("CLUSTER_NAME", clusterName),
                Map.entry("BACKUP_NAME", backupName)
        ));

        return templateEngine.process("psmdb-restore.yaml.txt", context);
    }
}

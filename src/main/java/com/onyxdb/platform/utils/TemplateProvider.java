package com.onyxdb.platform.utils;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import io.fabric8.kubernetes.api.model.Quantity;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import static com.onyxdb.platform.core.clusters.ClusterMapper.DEFAULT_NAMESPACE;

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
            String secretsUsersName,
            String vectorConfigName,
            String replsetName,
            int replsetSize,
            double vcpu,
            long ram
    ) {
        var mongodMemory = Quantity.fromNumericalAmount(BigDecimal.valueOf(ram), "Gi");

        Context context = new Context();
        context.setVariables(Map.ofEntries(
                Map.entry("METADATA_NAME", metadataName),
                Map.entry("SECRETS_USERS_NAME", secretsUsersName),
                Map.entry("VECTOR_CONFIG_NAME", vectorConfigName),
                Map.entry("REPLSET_NAME", replsetName),
                Map.entry("REPLSET_SIZE", replsetSize),
                Map.entry("MONGOD_CPU", vcpu),
                Map.entry("MONGOD_MEMORY", mongodMemory)
        ));

        return templateEngine.process("psmdb-cr.yaml.txt", context);
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
                Map.entry("CLUSTER_ID", clusterId),
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
}

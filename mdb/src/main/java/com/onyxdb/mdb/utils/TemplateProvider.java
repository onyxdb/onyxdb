package com.onyxdb.mdb.utils;

import java.util.Map;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

public class TemplateProvider {
    private static final String MONGO_VECTOR_CONFIG_MAP_TEMPLATE_PATH = "mongo-vector-config-map-template.txt";
    private static final String PSMDB_CR_TEMPLATE_PATH = "psmdb-cr-template.txt";

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

        return templateEngine.process(MONGO_VECTOR_CONFIG_MAP_TEMPLATE_PATH, context);
    }

    public String buildPsmdbCr(
            String metadataName,
            String secretsUsersName,
            String vectorConfigName
    ) {
        Context context = new Context();
        context.setVariables(Map.ofEntries(
                Map.entry("METADATA_NAME", metadataName),
                Map.entry("SECRETS_USERS_NAME", secretsUsersName),
                Map.entry("VECTOR_CONFIG_NAME", vectorConfigName)
        ));

        return templateEngine.process(PSMDB_CR_TEMPLATE_PATH, context);
    }
}

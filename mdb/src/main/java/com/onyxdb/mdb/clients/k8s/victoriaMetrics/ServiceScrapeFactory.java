package com.onyxdb.mdb.clients.k8s.victoriaMetrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.GenericKubernetesResourceBuilder;

import com.onyxdb.mdb.utils.ObjectMapperUtils;

public class ServiceScrapeFactory {
    public static final String GROUP = "operator.victoriametrics.com";
    public static final String VERSION = "v1beta1";
    public static final String API_VERSION = GROUP + "/" + VERSION;
    public static final String KIND = "VMServiceScrape";

    private final ObjectMapper objectMapper;

    public ServiceScrapeFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public GenericKubernetesResource buildResource(
            String name,
            ServiceScrape serviceScrape
    ) {
        var spec = ObjectMapperUtils.convertToMap(objectMapper, serviceScrape);

        return new GenericKubernetesResourceBuilder()
                .withApiVersion(API_VERSION)
                .withKind(KIND)
                .withNewMetadata()
                .withName(getPreparedName(name))
                .endMetadata()
                .addToAdditionalProperties("spec", spec)
                .build();
    }

    public static String getPreparedName(String name) {
        return String.format("%s-scrape", name);
    }
}

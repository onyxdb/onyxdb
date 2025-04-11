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

    public GenericKubernetesResource buildResource(ServiceScrape serviceScrape) {
        var specMap = ObjectMapperUtils.convertToMap(objectMapper, serviceScrape.spec());

        return new GenericKubernetesResourceBuilder()
                .withApiVersion(API_VERSION)
                .withKind(KIND)
                .withNewMetadata()
                .withName(getPreparedName(serviceScrape.name()))
                .endMetadata()
                .addToAdditionalProperties("spec", specMap)
                .build();
    }

    public static String getPreparedName(String name) {
        return String.format("%s-scrape", name);
    }
}

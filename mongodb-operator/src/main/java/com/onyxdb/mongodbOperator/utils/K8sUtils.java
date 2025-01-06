package com.onyxdb.mongodbOperator.utils;

import java.util.Map;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.CustomResource;

/**
 * @author foxleren
 */
public final class K8sUtils {
    private static final String APP_LABEL = "managed-mongodb";
    private static final String MANAGED_BY = "onyxdb-mongodb-operator";

    public static <T extends CustomResource<?, ?>> ObjectMeta enrichResourceMeta(
            T primary,
            String resourceNamePrefix)
    {
        ObjectMeta primaryMeta = primary.getMetadata();
        return new ObjectMetaBuilder()
                .withNamespace(primaryMeta.getNamespace())
                .withName(buildResourceName(resourceNamePrefix, primaryMeta.getName()))
                .withLabels(LabelsUtil.getClusterLabels(primary.getMetadata().getName()))
//                .withLabels(getSelectorLabels())
                .build();
    }

//    public static Map<String, String> getSelectorLabels() {
//        return Map.ofEntries(
//                Map.entry("app", APP_LABEL),
//                Map.entry("managedBy", MANAGED_BY)
//        );
//    }

    public static String buildResourceName(String resourceNamePrefix, String metaName) {
        return String.format("%s-%s", resourceNamePrefix, metaName);
    }
}

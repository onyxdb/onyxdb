package com.onyxdb.mongodbOperator.utils;

import java.util.Map;

import io.fabric8.kubernetes.client.CustomResource;

/**
 * Recommended labels:
 * <a href="https://kubernetes.io/docs/concepts/overview/working-with-objects/common-labels/">docs</a>.
 * <p>
 * Reserved labels: <a href="https://kubernetes.io/docs/reference/labels-annotations-taints/">docs</a>.
 *
 * @author foxleren
 */
public class LabelsUtil {
    public static final String APP_KUBERNETES_LABEL_PREFIX = "app.kubernetes.io/";

    public static final String APP_KUBERNETES_NAME_LABEL = APP_KUBERNETES_LABEL_PREFIX + "name";
    public static final String APP_KUBERNETES_INSTANCE_LABEL = APP_KUBERNETES_LABEL_PREFIX + "instance";
    public static final String APP_KUBERNETES_COMPONENT_LABEL = APP_KUBERNETES_LABEL_PREFIX + "component";
    public static final String APP_KUBERNETES_PART_OF_LABEL = APP_KUBERNETES_LABEL_PREFIX + "part-of";
    public static final String APP_KUBERNETES_MANAGED_BY_LABEL = APP_KUBERNETES_LABEL_PREFIX + "managed-by";

    public static final String APP_KUBERNETES_NAME_LABEL_VALUE = "onyxdb-managed-mongodb";
    public static final String APP_KUBERNETES_COMPONENT_LABEL_VALUE = "database";
    public static final String APP_KUBERNETES_PART_OF_LABEL_VALUE = "onyxdb-managed-mongodb";
    public static final String APP_KUBERNETES_MANAGED_BY_LABEL_VALUE = "onyxdb-managed-mongodb-operator";

    public static Map<String, String> getClusterLabels(String crName) {
        return Map.ofEntries(
                Map.entry(APP_KUBERNETES_NAME_LABEL, APP_KUBERNETES_NAME_LABEL_VALUE),
                Map.entry(APP_KUBERNETES_INSTANCE_LABEL, crName),
                Map.entry(APP_KUBERNETES_COMPONENT_LABEL, APP_KUBERNETES_COMPONENT_LABEL_VALUE),
                Map.entry(APP_KUBERNETES_PART_OF_LABEL, APP_KUBERNETES_PART_OF_LABEL_VALUE),
                Map.entry(APP_KUBERNETES_MANAGED_BY_LABEL, APP_KUBERNETES_MANAGED_BY_LABEL_VALUE)
        );
    }

    public static <T extends CustomResource<?, ?>> Map<String, String> getClusterLabels(T primary) {
        return getClusterLabels(primary.getMetadata().getName());
    }
}

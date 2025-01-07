package com.onyxdb.mongodbOperator.utils;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.CustomResource;

/**
 * @author foxleren
 */
public final class MetaUtils {
    private static final String RESOURCE_INSTANCE_NAME_PREFIX = "managed-mongodb-";

    public static <T extends CustomResource<?, ?>> ObjectMeta createMetaFromPrimary(T primary) {
        ObjectMeta primaryMeta = primary.getMetadata();
        return new ObjectMetaBuilder()
                .withNamespace(primaryMeta.getNamespace())
                .withName(MetaUtils.getResourceInstanceNameWithPrefix(primary))
                .withLabels(LabelsUtils.getClusterLabels(primaryMeta.getName()))
                .build();
    }

    public static <T extends CustomResource<?, ?>> String getResourceInstanceNameWithPrefix(T primary) {
        return RESOURCE_INSTANCE_NAME_PREFIX + primary.getMetadata().getName();
    }
}

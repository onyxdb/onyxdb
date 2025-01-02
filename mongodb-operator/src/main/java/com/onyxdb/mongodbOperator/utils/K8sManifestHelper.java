package com.onyxdb.mongodbOperator.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.fabric8.kubernetes.api.model.ManagedFieldsEntry;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.CustomResource;


/**
 * @author foxleren
 */

public final class K8sManifestHelper {
    private static final String OPERATOR_NAME = "onyxdb-mongodb-k8s-operator";

    // TODO refactor methods
    private static final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    public static <T extends CustomResource<?, ?>> ObjectMetaBuilder fromPrimary(T primary, String component) {
        return new ObjectMetaBuilder()
                .withNamespace(primary.getMetadata().getNamespace())
                .withManagedFields((List<ManagedFieldsEntry>) null)
                .addToLabels("component", component)
                .addToLabels("name", primary.getMetadata().getName())
                .withName(primary.getMetadata().getName() + "-" + component)
                .addToLabels("managedBy", OPERATOR_NAME);
    }

    public static <T> T loadTemplate(Class<T> clazz, String resource) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = K8sManifestHelper.class.getClassLoader();
        }

        try (InputStream is = cl.getResourceAsStream(resource)) {
            return loadTemplate(clazz, is);
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to load classpath resource '" + resource + "': " + ioe.getMessage());
        }
    }

    public static <T> T loadTemplate(Class<T> clazz, InputStream is) throws IOException {
        return objectMapper.readValue(is, clazz);
    }
}

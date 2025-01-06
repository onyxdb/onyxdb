package com.onyxdb.mongodbOperator.resources;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

/**
 * @author foxleren
 */
@Group("onyxdb.com")
@Version("v1")
public class ManagedMongoDB extends CustomResource<MongoDbSpec, MongodbStatus> implements Namespaced {
}

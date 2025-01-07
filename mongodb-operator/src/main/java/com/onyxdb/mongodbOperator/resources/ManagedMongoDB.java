package com.onyxdb.mongodbOperator.resources;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Kind;
import io.fabric8.kubernetes.model.annotation.Version;

import com.onyxdb.mongodbOperator.status.MongoStatus;

/**
 * @author foxleren
 */
@Group("onyxdb.com")
@Version("v1")
@Kind("ManagedMongoDB")
public class ManagedMongoDB extends CustomResource<MongoSpec, MongoStatus> implements Namespaced {
}

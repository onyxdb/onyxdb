package com.onyxdb.mongodbK8sOperator.crds;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

/**
 * @author foxleren
 */
@Group("onyxdb.com")
@Version("v1")
public class ManagedMongoDbResource extends CustomResource<ManagedMongoDbSpec, ManagedMongoDbStatus> implements Namespaced {
    @JsonIgnore
    public String getMongoDbClusterServiceName() {
        return this.getMetadata().getName() + "-" + ManagedMongoDbServiceResource.COMPONENT;
    }
}

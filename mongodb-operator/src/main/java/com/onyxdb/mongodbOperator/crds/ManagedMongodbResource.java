package com.onyxdb.mongodbOperator.crds;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

/**
 * @author foxleren
 */
@Group("onyxdb.com")
@Version("v1")
public class ManagedMongodbResource extends CustomResource<ManagedMongodbSpec, ManagedMongodbStatus> implements Namespaced {
//    @JsonIgnore
//    public String getManagedMongodbServiceName() {
//        return this.getMetadata().getName() + "-" + ManagedMongodbServiceResource.COMPONENT;
//    }
}

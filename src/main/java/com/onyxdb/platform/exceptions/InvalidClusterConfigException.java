package com.onyxdb.platform.exceptions;

import com.onyxdb.platform.core.clusters.models.ClusterVersion;

/**
 * @author foxleren
 */
public class InvalidClusterConfigException extends BadRequestException {
    public InvalidClusterConfigException(ClusterVersion clusterVersion) {
        super(String.format(
                "Config for version=%s is not provided",
                clusterVersion.value()
        ));
    }
}

package com.onyxdb.mdb.exceptions;

/**
 * @author foxleren
 */
public class InvalidClusterTypeException extends BadRequestException {
    public InvalidClusterTypeException(String clusterType) {
        super(String.format("Failed to parse cluster type because value is not valid. ClusterType={%s}", clusterType));
    }
}

package com.onyxdb.platform.idm.v1;

/**
 * @author ArtemFed
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
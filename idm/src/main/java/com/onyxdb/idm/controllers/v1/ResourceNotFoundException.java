package com.onyxdb.idm.controllers.v1;

/**
 * @author ArtemFed
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
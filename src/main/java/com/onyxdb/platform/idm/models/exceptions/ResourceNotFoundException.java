package com.onyxdb.platform.idm.models.exceptions;

/**
 * @author ArtemFed
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
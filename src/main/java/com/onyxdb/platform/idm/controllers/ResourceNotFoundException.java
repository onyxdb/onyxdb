package com.onyxdb.platform.idm.controllers;

/**
 * @author ArtemFed
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
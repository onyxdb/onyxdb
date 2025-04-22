package com.onyxdb.platform.exceptions;

/**
 * @author foxleren
 */
public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String message) {
        super(message);
    }
}

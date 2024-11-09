package com.onyxdb.mdb.exceptions;

/**
 * @author foxleren
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}

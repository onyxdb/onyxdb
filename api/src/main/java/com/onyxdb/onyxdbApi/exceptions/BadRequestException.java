package com.onyxdb.onyxdbApi.exceptions;

/**
 * @author foxleren
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}

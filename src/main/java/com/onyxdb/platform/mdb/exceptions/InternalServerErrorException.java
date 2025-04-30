package com.onyxdb.platform.mdb.exceptions;

/**
 * @author foxleren
 */
public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String message) {
        super(message);
    }
}

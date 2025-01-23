package com.onyxdb.mdb.exceptions;

/**
 * @author foxleren
 */
public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String message) {
        super(message);
    }
}

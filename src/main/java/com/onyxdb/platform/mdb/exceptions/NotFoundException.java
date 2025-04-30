package com.onyxdb.platform.mdb.exceptions;

/**
 * @author foxleren
 */
public class NotFoundException extends BadRequestException {
    public NotFoundException(String message) {
        super(message);
    }
}

package com.onyxdb.platform.idm.models.exceptions;

import com.onyxdb.platform.mdb.exceptions.MdbException;

/**
 * @author ArtemFed
 */
public class UnauthorizedException extends MdbException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

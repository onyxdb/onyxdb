package com.onyxdb.platform.idm.models.exceptions;

import com.onyxdb.platform.mdb.exceptions.MdbException;

/**
 * @author foxleren
 */
public class UnauthorizedException extends MdbException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

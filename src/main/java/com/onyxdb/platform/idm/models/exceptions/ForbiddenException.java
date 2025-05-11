package com.onyxdb.platform.idm.models.exceptions;

import com.onyxdb.platform.mdb.exceptions.MdbException;

/**
 * @author ArtemFed
 */
public class ForbiddenException extends MdbException {
    public ForbiddenException(String message) {
        super(message);
    }
}

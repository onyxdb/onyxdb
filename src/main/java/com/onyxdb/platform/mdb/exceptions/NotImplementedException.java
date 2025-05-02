package com.onyxdb.platform.mdb.exceptions;

/**
 * @author foxleren
 */
public class NotImplementedException extends InternalServerErrorException {
    public NotImplementedException() {
        super();
    }

    public NotImplementedException(Throwable cause) {
        super(cause);
    }

    public NotImplementedException(String message) {
        super(message);
    }
}

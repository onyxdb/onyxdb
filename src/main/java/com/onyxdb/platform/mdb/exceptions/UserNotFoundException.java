package com.onyxdb.platform.mdb.exceptions;

import java.util.UUID;

/**
 * @author foxleren
 */
public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(UUID userId) {
        super(String.format("User with id '%s' is not found", userId));
    }

    public UserNotFoundException(String userName) {
        super(String.format("User with name '%s' is not found", userName));
    }
}

package com.onyxdb.platform.mdb.exceptions;

/**
 * @author foxleren
 */
public class DatabaseNotFoundException extends NotFoundException {
    public DatabaseNotFoundException(String databaseName) {
        super(String.format("Database with name '%s' is not found", databaseName));
    }
}

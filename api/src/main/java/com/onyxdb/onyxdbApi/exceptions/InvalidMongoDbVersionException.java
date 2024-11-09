package com.onyxdb.onyxdbApi.exceptions;

/**
 * @author foxleren
 */
public class InvalidMongoDbVersionException extends BadRequestException {
    public InvalidMongoDbVersionException(String version) {
        super(String.format("Failed to parse mongodb version because value is not valid. Version={%s}", version));
    }
}

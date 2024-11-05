package com.onyxdb.onyxdbApi.exceptions;

/**
 * @author foxleren
 */
public class InvalidMongoDbVersionException extends BadRequestException {
    public InvalidMongoDbVersionException(String version) {
        super(String.format("Got invalid mongodb version to parse. Version={%s}", version));
    }
}

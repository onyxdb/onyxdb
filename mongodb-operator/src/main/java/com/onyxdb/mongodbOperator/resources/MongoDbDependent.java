package com.onyxdb.mongodbOperator.resources;

/**
 * @author foxleren
 */
public interface MongoDbDependent {
    String getUniqueDependentName(ManagedMongoDB managedMongoDB);
}

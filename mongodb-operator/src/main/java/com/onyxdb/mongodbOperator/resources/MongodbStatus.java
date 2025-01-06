package com.onyxdb.mongodbOperator.resources;

import io.javaoperatorsdk.operator.api.ObservedGenerationAwareStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author foxleren
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MongodbStatus extends ObservedGenerationAwareStatus {
    private MongoDbState state;
    private String message;

    public MongodbStatus(MongoDbState state) {
        this.state = state;
        this.message = "";
    }

    public static MongodbStatus withErrorState(String message) {
        return new MongodbStatus(MongoDbState.ERROR, message);
    }
}

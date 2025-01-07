package com.onyxdb.mongodbOperator.status;

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
public class MongoStatus extends ObservedGenerationAwareStatus {
    private MongoState state;
    private String message;

    public MongoStatus(MongoState state) {
        this.state = state;
        this.message = "";
    }

    public static MongoStatus withErrorState(String message) {
        return new MongoStatus(MongoState.ERROR, message);
    }
}

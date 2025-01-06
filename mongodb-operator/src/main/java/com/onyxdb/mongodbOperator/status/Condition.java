package com.onyxdb.mongodbOperator.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author foxleren
 */
@Getter
@Setter
@RequiredArgsConstructor
public class Condition {
    private final String type;
    private final ConditionStatus status;
    private final String message;
}

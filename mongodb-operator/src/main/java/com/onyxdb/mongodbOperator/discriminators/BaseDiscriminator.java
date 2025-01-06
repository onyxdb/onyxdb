package com.onyxdb.mongodbOperator.discriminators;

import lombok.RequiredArgsConstructor;

/**
 * @author foxleren
 */
@RequiredArgsConstructor
public class BaseDiscriminator {
    private final String resourceName;
    private final String metaName;

    public String getUniqueResourceName() {
        return resourceName + "-" + metaName;
    }
}

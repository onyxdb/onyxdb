package com.onyxdb.mdb.controllers.v1.models;

/**
 * @author foxleren
 */
public record QuotaRes(
        String resourceType,
        Integer quoted,
        String units
) {
}

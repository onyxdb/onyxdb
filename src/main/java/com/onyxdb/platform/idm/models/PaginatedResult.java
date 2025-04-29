package com.onyxdb.platform.idm.models;


import java.util.List;

/**
 * @author ArtemFed
 */
public record PaginatedResult<T>(
        List<T> data,
        int totalCount,
        int startPosition,
        int endPosition
) {
}

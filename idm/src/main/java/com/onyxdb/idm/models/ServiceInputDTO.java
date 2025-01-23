package com.onyxdb.idm.models;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

/**
 * @author ArtemFed
 */
@Data
@Builder
@EqualsAndHashCode
@FieldNameConstants
public class ServiceInputDTO {
    private String name;
    private String type;
    private UUID projectId;
}

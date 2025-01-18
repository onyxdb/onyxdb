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
public class Service {
    private UUID id;
    private String name;
    private String type;
    private UUID projectId;
    private Project project;
}

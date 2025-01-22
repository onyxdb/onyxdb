package com.onyxdb.idm.models;

import java.util.List;
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
public class ProjectDTO {
    private UUID id;
    private String name;
    private UUID organizationId;
    private OrganizationDTO organization;
    private List<ServiceDTO> services;
}

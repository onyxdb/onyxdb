package com.onyxdb.idm.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PermissionDTO {
    private UUID id;
    private String actionType;
    private String resourceType;
    private String[] resourceFields;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

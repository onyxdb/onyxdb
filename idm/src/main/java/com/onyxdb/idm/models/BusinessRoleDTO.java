package com.onyxdb.idm.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BusinessRoleDTO {
    private UUID id;
    private String name;
    private String description;
    private UUID parentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

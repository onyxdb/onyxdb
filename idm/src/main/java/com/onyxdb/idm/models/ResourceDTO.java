package com.onyxdb.idm.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ResourceDTO {
    private UUID id;
    private String resourceType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

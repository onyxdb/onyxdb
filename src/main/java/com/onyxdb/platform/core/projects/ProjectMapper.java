package com.onyxdb.platform.core.projects;

import java.util.List;
import java.util.UUID;

import org.jooq.Record;

import com.onyxdb.platform.generated.jooq.tables.records.ProjectsRecord;
import com.onyxdb.platform.generated.openapi.models.CreateProjectRequestDTO;
import com.onyxdb.platform.generated.openapi.models.ListProjectsResponseDTO;
import com.onyxdb.platform.generated.openapi.models.ProjectDTO;
import com.onyxdb.platform.generated.openapi.models.UpdateProjectRequestDTO;

/**
 * @author foxleren
 */
public final class ProjectMapper {
    public static Project fromJooqRecord(Record r) {
        ProjectsRecord rr = r.into(ProjectsRecord.class);
        return new Project(
                rr.getId(),
                rr.getName(),
                rr.getDescription(),
                rr.getProductId(),
                rr.getIsArchived()
        );
    }

    public static ProjectsRecord toJooqProjectsRecord(Project p) {
        return new ProjectsRecord(
                p.id(),
                p.name(),
                p.description(),
                p.productId(),
                p.isArchived()
        );
    }

    public static ProjectDTO toV1ProjectResponse(Project p) {
        return new ProjectDTO(
                p.id(),
                p.name(),
                p.description(),
                p.productId()
        );
    }

    public static ListProjectsResponseDTO toV1ListProjectsResponse(List<Project> p) {
        return new ListProjectsResponseDTO(
                p.stream()
                        .map(ProjectMapper::toV1ProjectResponse)
                        .toList()
        );
    }

    public static Project fromV1CreateProjectRequest(CreateProjectRequestDTO r) {
        return new Project(
                UUID.randomUUID(),
                r.getName(),
                r.getDescription(),
                r.getProductId(),
                false
        );
    }

    public static UpdateProject fromV1UpdateProjectRequest(UUID id, UpdateProjectRequestDTO r) {
        return new UpdateProject(
                id,
                r.getName(),
                r.getDescription()
        );
    }
}

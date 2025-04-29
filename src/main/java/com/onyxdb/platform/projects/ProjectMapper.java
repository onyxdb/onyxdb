package com.onyxdb.platform.projects;

import java.util.UUID;

import org.jooq.Record;

import com.onyxdb.platform.generated.jooq.tables.records.ProjectsRecord;
import com.onyxdb.platform.generated.openapi.models.CreateProjectRequestDTO;
import com.onyxdb.platform.generated.openapi.models.ProjectDTO;
import com.onyxdb.platform.generated.openapi.models.UpdateProjectRequestDTO;

/**
 * @author foxleren
 */
public class ProjectMapper {
    public ProjectDTO projectToProjectDTO(Project p) {
        return new ProjectDTO(
                p.id(),
                p.name(),
                p.description(),
                p.productId()
        );
    }

    public static Project jooqRecordToProject(Record r) {
        ProjectsRecord rr = r.into(ProjectsRecord.class);
        return new Project(
                rr.getId(),
                rr.getName(),
                rr.getDescription(),
                rr.getProductId(),
                rr.getIsDeleted()
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

    public ProjectToCreate createProjectRequestDTOtoProjectToCreate(CreateProjectRequestDTO rq) {
        return new ProjectToCreate(
                rq.getName(),
                rq.getDescription(),
                rq.getProductId()
        );
    }

    public Project projectToCreateToProject(ProjectToCreate projectToCreate) {
        return Project.create(
                projectToCreate.name(),
                projectToCreate.description(),
                projectToCreate.productId()
        );
    }
}

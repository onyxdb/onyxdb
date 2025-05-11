package com.onyxdb.platform.mdb.projects;

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
                p.productId(),
                p.namespace(),
                p.createdAt(),
                p.createdBy(),
                p.isDeleted(),
                p.deletedAt(),
                p.deletedBy()
        );
    }

    public static Project jooqRecordToProject(Record r) {
        ProjectsRecord rr = r.into(ProjectsRecord.class);
        return new Project(
                rr.getId(),
                rr.getName(),
                rr.getDescription(),
                rr.getProductId(),
                rr.getNamespace(),
                rr.getCreatedAt(),
                rr.getCreatedBy(),
                rr.getIsDeleted(),
                rr.getDeletedAt(),
                rr.getDeletedBy()
        );
    }

    public static ProjectsRecord toJooqProjectsRecord(Project p) {
        return new ProjectsRecord(
                p.id(),
                p.name(),
                p.description(),
                p.productId(),
                p.namespace(),
                p.createdAt(),
                p.createdBy(),
                p.isDeleted(),
                p.deletedAt(),
                p.deletedBy()
        );
    }

    public static UpdateProject updateProjectRequestDTOtoUpdateProject(UUID productId, UpdateProjectRequestDTO rq) {
        return new UpdateProject(
                productId,
                rq.getDescription()
        );
    }

    public CreateProject createProjectRequestDTOtoProjectToCreate(CreateProjectRequestDTO r, UUID createdBy) {
        return new CreateProject(
                r.getName(),
                r.getDescription(),
                r.getProductId(),
                r.getNamespace(),
                createdBy
        );
    }

    public Project createProjectToProject(CreateProject c) {
        return Project.create(
                c.name(),
                c.description(),
                c.productId(),
                c.namespace(),
                c.createdBy()
        );
    }
}

package com.onyxdb.mdb.core.projects;

import java.util.List;
import java.util.UUID;

import org.jooq.Record;

import com.onyxdb.mdb.generated.jooq.tables.records.ProjectsRecord;
import com.onyxdb.mdb.generated.openapi.models.V1CreateProjectRequest;
import com.onyxdb.mdb.generated.openapi.models.V1ListProjectsResponse;
import com.onyxdb.mdb.generated.openapi.models.V1ProjectResponse;
import com.onyxdb.mdb.generated.openapi.models.V1UpdateProjectRequest;

/**
 * @author foxleren
 */
public final class ProjectConverter {
    public static Project fromJooqRecord(Record r) {
        ProjectsRecord rr = r.into(ProjectsRecord.class);
        return new Project(
                rr.getId(),
                rr.getName(),
                rr.getDescription(),
                rr.getIsArchived()
        );
    }

    public static ProjectsRecord toJooqProjectsRecord(Project p) {
        return new ProjectsRecord(
                p.id(),
                p.name(),
                p.description(),
                p.isArchived()
        );
    }

    public static V1ProjectResponse toV1ProjectResponse(Project p) {
        return new V1ProjectResponse(
                p.id(),
                p.name(),
                p.description(),
                p.isArchived()
        );
    }

    public static V1ListProjectsResponse toV1ListProjectsResponse(List<Project> p) {
        return new V1ListProjectsResponse(
                p.stream()
                        .map(ProjectConverter::toV1ProjectResponse)
                        .toList()
        );
    }

    public static Project fromV1CreateProjectRequest(V1CreateProjectRequest r) {
        return new Project(
                UUID.randomUUID(),
                r.getName(),
                r.getDescription(),
                false
        );
    }

    public static UpdateProject fromV1UpdateProjectRequest(UUID id, V1UpdateProjectRequest r) {
        return new UpdateProject(
                id,
                r.getName(),
                r.getDescription()
        );
    }
}

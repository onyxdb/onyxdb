package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.ProjectTable;
import com.onyxdb.idm.models.Project;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProjectPostgresRepository implements ProjectRepository {

    private final DSLContext dslContext;
    private final ProjectTable projectTable = Tables.PROJECT_TABLE;

    @Override
    public Optional<Project> findById(UUID id) {
        return dslContext.selectFrom(projectTable)
                .where(projectTable.ID.eq(id))
                .fetchOptional(record -> new Project(
                        record.getId(),
                        record.getName(),
                        record.getDescription(),
                        record.getCreatedAt(),
                        record.getUpdatedAt(),
                        record.getResourceId(),
                        record.getOrganizationId(),
                        record.getOwnerId()
                ));
    }

    @Override
    public List<Project> findAll() {
        return dslContext.selectFrom(projectTable)
                .fetch(record -> new Project(
                        record.getId(),
                        record.getName(),
                        record.getDescription(),
                        record.getCreatedAt(),
                        record.getUpdatedAt(),
                        record.getResourceId(),
                        record.getOrganizationId(),
                        record.getOwnerId()
                ));
    }

    @Override
    public List<Project> findByOrganizationId(UUID organizationId) {
        return dslContext.selectFrom(projectTable)
                .where(projectTable.ORGANIZATION_ID.eq(organizationId))
                .fetch(record -> new Project(
                        record.getId(),
                        record.getName(),
                        record.getDescription(),
                        record.getCreatedAt(),
                        record.getUpdatedAt(),
                        record.getResourceId(),
                        record.getOrganizationId(),
                        record.getOwnerId()
                ));
    }

    @Override
    public void create(Project project) {
        dslContext.insertInto(projectTable)
                .set(projectTable.ID, project.id())
                .set(projectTable.NAME, project.name())
                .set(projectTable.DESCRIPTION, project.description())
                .set(projectTable.CREATED_AT, project.createdAt())
                .set(projectTable.UPDATED_AT, project.updatedAt())
                .set(projectTable.RESOURCE_ID, project.resourceId())
                .set(projectTable.ORGANIZATION_ID, project.organizationId())
                .set(projectTable.OWNER_ID, project.ownerId())
                .execute();
    }

    @Override
    public void update(Project project) {
        dslContext.update(projectTable)
                .set(projectTable.NAME, project.name())
                .set(projectTable.DESCRIPTION, project.description())
                .set(projectTable.UPDATED_AT, project.updatedAt())
                .set(projectTable.RESOURCE_ID, project.resourceId())
                .set(projectTable.ORGANIZATION_ID, project.organizationId())
                .set(projectTable.OWNER_ID, project.ownerId())
                .where(projectTable.ID.eq(project.id()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(projectTable)
                .where(projectTable.ID.eq(id))
                .execute();
    }
}
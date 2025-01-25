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
                .fetchOptional(record -> Project.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .description(record.getDescription())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .resourceId(record.getResourceId())
                        .organizationId(record.getOrganizationId())
                        .ownerId(record.getOwnerId())
                        .build());
    }

    @Override
    public List<Project> findAll() {
        return dslContext.selectFrom(projectTable)
                .fetch(record -> Project.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .description(record.getDescription())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .resourceId(record.getResourceId())
                        .organizationId(record.getOrganizationId())
                        .ownerId(record.getOwnerId())
                        .build());
    }

    @Override
    public List<Project> findByOrganizationId(UUID organizationId) {
        return dslContext.selectFrom(projectTable)
                .where(projectTable.ORGANIZATION_ID.eq(organizationId))
                .fetch(record -> Project.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .description(record.getDescription())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .resourceId(record.getResourceId())
                        .organizationId(record.getOrganizationId())
                        .ownerId(record.getOwnerId())
                        .build());
    }

    @Override
    public void create(Project project) {
        dslContext.insertInto(projectTable)
                .set(projectTable.ID, project.getId())
                .set(projectTable.NAME, project.getName())
                .set(projectTable.DESCRIPTION, project.getDescription())
                .set(projectTable.CREATED_AT, project.getCreatedAt())
                .set(projectTable.UPDATED_AT, project.getUpdatedAt())
                .set(projectTable.RESOURCE_ID, project.getResourceId())
                .set(projectTable.ORGANIZATION_ID, project.getOrganizationId())
                .set(projectTable.OWNER_ID, project.getOwnerId())
                .execute();
    }

    @Override
    public void update(Project project) {
        dslContext.update(projectTable)
                .set(projectTable.NAME, project.getName())
                .set(projectTable.DESCRIPTION, project.getDescription())
                .set(projectTable.UPDATED_AT, project.getUpdatedAt())
                .set(projectTable.RESOURCE_ID, project.getResourceId())
                .set(projectTable.ORGANIZATION_ID, project.getOrganizationId())
                .set(projectTable.OWNER_ID, project.getOwnerId())
                .where(projectTable.ID.eq(project.getId()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(projectTable)
                .where(projectTable.ID.eq(id))
                .execute();
    }
}
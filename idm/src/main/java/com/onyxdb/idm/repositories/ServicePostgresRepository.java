package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.ServiceTable;
import com.onyxdb.idm.models.Service;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ServicePostgresRepository implements ServiceRepository {

    private final DSLContext dslContext;
    private final ServiceTable serviceTable = Tables.SERVICE_TABLE;

    @Override
    public Optional<Service> findById(UUID id) {
        return dslContext.selectFrom(serviceTable)
                .where(serviceTable.ID.eq(id))
                .fetchOptional(record -> Service.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .type(record.getType())
                        .description(record.getDescription())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .resourceId(record.getResourceId())
                        .projectId(record.getProjectId())
                        .ownerId(record.getOwnerId())
                        .build());
    }

    @Override
    public List<Service> findAll() {
        return dslContext.selectFrom(serviceTable)
                .fetch(record -> Service.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .type(record.getType())
                        .description(record.getDescription())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .resourceId(record.getResourceId())
                        .projectId(record.getProjectId())
                        .ownerId(record.getOwnerId())
                        .build());
    }

    @Override
    public List<Service> findByProjectId(UUID projectId) {
        return dslContext.selectFrom(serviceTable)
                .where(serviceTable.PROJECT_ID.eq(projectId))
                .fetch(record -> Service.builder()
                        .id(record.getId())
                        .name(record.getName())
                        .type(record.getType())
                        .description(record.getDescription())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .resourceId(record.getResourceId())
                        .projectId(record.getProjectId())
                        .ownerId(record.getOwnerId())
                        .build());
    }

    @Override
    public void create(Service service) {
        dslContext.insertInto(serviceTable)
                .set(serviceTable.ID, service.getId())
                .set(serviceTable.NAME, service.getName())
                .set(serviceTable.TYPE, service.getType())
                .set(serviceTable.DESCRIPTION, service.getDescription())
                .set(serviceTable.CREATED_AT, service.getCreatedAt())
                .set(serviceTable.UPDATED_AT, service.getUpdatedAt())
                .set(serviceTable.RESOURCE_ID, service.getResourceId())
                .set(serviceTable.PROJECT_ID, service.getProjectId())
                .set(serviceTable.OWNER_ID, service.getOwnerId())
                .execute();
    }

    @Override
    public void update(Service service) {
        dslContext.update(serviceTable)
                .set(serviceTable.NAME, service.getName())
                .set(serviceTable.TYPE, service.getType())
                .set(serviceTable.DESCRIPTION, service.getDescription())
                .set(serviceTable.UPDATED_AT, service.getUpdatedAt())
                .set(serviceTable.RESOURCE_ID, service.getResourceId())
                .set(serviceTable.PROJECT_ID, service.getProjectId())
                .set(serviceTable.OWNER_ID, service.getOwnerId())
                .where(serviceTable.ID.eq(service.getId()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(serviceTable)
                .where(serviceTable.ID.eq(id))
                .execute();
    }
}
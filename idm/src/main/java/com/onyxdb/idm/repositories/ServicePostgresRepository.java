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
                .fetchOptional(Service::fromDAO);
    }

    @Override
    public List<Service> findAll() {
        return dslContext.selectFrom(serviceTable)
                .fetch(Service::fromDAO);
    }

    @Override
    public List<Service> findByProjectId(UUID projectId) {
        return dslContext.selectFrom(serviceTable)
                .where(serviceTable.PROJECT_ID.eq(projectId))
                .fetch(Service::fromDAO);
    }

    @Override
    public void create(Service service) {
        dslContext.insertInto(serviceTable)
                .set(serviceTable.ID, service.id())
                .set(serviceTable.NAME, service.name())
                .set(serviceTable.TYPE, service.type())
                .set(serviceTable.DESCRIPTION, service.description())
                .set(serviceTable.CREATED_AT, service.createdAt())
                .set(serviceTable.UPDATED_AT, service.updatedAt())
                .set(serviceTable.RESOURCE_ID, service.resourceId())
                .set(serviceTable.PROJECT_ID, service.projectId())
                .set(serviceTable.OWNER_ID, service.ownerId())
                .execute();
    }

    @Override
    public void update(Service service) {
        dslContext.update(serviceTable)
                .set(serviceTable.NAME, service.name())
                .set(serviceTable.TYPE, service.type())
                .set(serviceTable.DESCRIPTION, service.description())
                .set(serviceTable.UPDATED_AT, service.updatedAt())
                .set(serviceTable.RESOURCE_ID, service.resourceId())
                .set(serviceTable.PROJECT_ID, service.projectId())
                .set(serviceTable.OWNER_ID, service.ownerId())
                .where(serviceTable.ID.eq(service.id()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(serviceTable)
                .where(serviceTable.ID.eq(id))
                .execute();
    }
}
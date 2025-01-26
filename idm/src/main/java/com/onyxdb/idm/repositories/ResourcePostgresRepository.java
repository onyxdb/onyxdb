package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.ResourceTable;
import com.onyxdb.idm.models.Resource;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author ArtemFed
 */
@Repository
@RequiredArgsConstructor
public class ResourcePostgresRepository implements ResourceRepository {
    private final DSLContext dslContext;
    private final ResourceTable resourceTable = Tables.RESOURCE_TABLE;

    @Override
    public Optional<Resource> findById(UUID id) {
        return dslContext.selectFrom(resourceTable)
                .where(resourceTable.ID.eq(id))
                .fetchOptional(Resource::fromDAO);
    }

    @Override
    public List<Resource> findAll() {
        return dslContext.selectFrom(resourceTable)
                .fetch(Resource::fromDAO);
    }

    @Override
    public void create(Resource resource) {
        dslContext.insertInto(resourceTable)
                .set(resourceTable.ID, resource.id())
                .set(resourceTable.RESOURCE_TYPE, resource.resourceType())
                .set(resourceTable.CREATED_AT, resource.createdAt())
                .set(resourceTable.UPDATED_AT, resource.updatedAt())
                .execute();
    }

    @Override
    public void update(Resource resource) {
        dslContext.update(resourceTable)
                .set(resourceTable.RESOURCE_TYPE, resource.resourceType())
                .set(resourceTable.UPDATED_AT, resource.updatedAt())
                .where(resourceTable.ID.eq(resource.id()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(resourceTable)
                .where(resourceTable.ID.eq(id))
                .execute();
    }
}
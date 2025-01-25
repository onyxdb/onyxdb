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

@Repository
@RequiredArgsConstructor
public class ResourcePostgresRepository implements ResourceRepository {

    private final DSLContext dslContext;
    private final ResourceTable resourceTable = Tables.RESOURCE_TABLE;

    @Override
    public Optional<Resource> findById(UUID id) {
        return dslContext.selectFrom(resourceTable)
                .where(resourceTable.ID.eq(id))
                .fetchOptional(record -> Resource.builder()
                        .id(record.getId())
                        .resourceType(record.getResourceType())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .build());
    }

    @Override
    public List<Resource> findAll() {
        return dslContext.selectFrom(resourceTable)
                .fetch(record -> Resource.builder()
                        .id(record.getId())
                        .resourceType(record.getResourceType())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .build());
    }

    @Override
    public void create(Resource resource) {
        dslContext.insertInto(resourceTable)
                .set(resourceTable.ID, resource.getId())
                .set(resourceTable.RESOURCE_TYPE, resource.getResourceType())
                .set(resourceTable.CREATED_AT, resource.getCreatedAt())
                .set(resourceTable.UPDATED_AT, resource.getUpdatedAt())
                .execute();
    }

    @Override
    public void update(Resource resource) {
        dslContext.update(resourceTable)
                .set(resourceTable.RESOURCE_TYPE, resource.getResourceType())
                .set(resourceTable.UPDATED_AT, resource.getUpdatedAt())
                .where(resourceTable.ID.eq(resource.getId()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(resourceTable)
                .where(resourceTable.ID.eq(id))
                .execute();
    }
}
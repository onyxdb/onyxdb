package com.onyxdb.mdb.resources;

import java.util.List;

import org.jooq.DSLContext;

import com.onyxdb.mdb.generated.jooq.tables.records.ResourcesRecord;

import static com.onyxdb.mdb.generated.jooq.Tables.RESOURCES;

public class ResourcePostgresRepository implements ResourceRepository {
    private final DSLContext dslContext;
    private final ResourceMapper resourceMapper;

    public ResourcePostgresRepository(DSLContext dslContext, ResourceMapper resourceMapper) {
        this.dslContext = dslContext;
        this.resourceMapper = resourceMapper;
    }

    @Override
    public List<Resource> listResources(ResourceFilter filter) {
        return dslContext.select()
                .from(RESOURCES)
                .where(filter.buildCondition())
                .fetch(r -> resourceMapper.map(r.into(ResourcesRecord.class)));
    }
}

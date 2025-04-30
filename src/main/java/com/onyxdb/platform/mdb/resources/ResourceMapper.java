package com.onyxdb.platform.mdb.resources;

import org.jooq.Record;

import com.onyxdb.platform.generated.jooq.tables.records.ResourcesRecord;
import com.onyxdb.platform.generated.openapi.models.ResourceUnit;
import com.onyxdb.platform.mdb.quotas.QuotaProvider;

import static com.onyxdb.platform.generated.jooq.Tables.RESOURCES;

public class ResourceMapper {
    public com.onyxdb.platform.generated.openapi.models.Resource map(Resource r) {
        return new com.onyxdb.platform.generated.openapi.models.Resource(
                r.id(),
                r.name(),
                r.description(),
                com.onyxdb.platform.generated.openapi.models.ResourceType.fromValue(r.type().value()),
                ResourceUnit.fromValue(r.type().getUnit().value())
        );
    }

    public Resource fromRecord(Record r) {
        return new Resource(
                r.get(RESOURCES.ID),
                r.get(RESOURCES.NAME),
                r.get(RESOURCES.DESCRIPTION),
                ResourceType.fromValue(r.get(RESOURCES.TYPE).getLiteral()),
                QuotaProvider.R.fromValue(r.get(RESOURCES.PROVIDER).getLiteral()));
    }

    public Resource map(ResourcesRecord r) {
        return new Resource(
                r.getId(),
                r.getName(),
                r.getDescription(),
                ResourceType.fromValue(r.getType().getLiteral()),
                QuotaProvider.R.fromValue(r.getProvider().getLiteral())
        );
    }
}

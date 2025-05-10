package com.onyxdb.platform.mdb.resources;

import org.jooq.Record;

import com.onyxdb.platform.generated.jooq.tables.records.ResourcesRecord;
import com.onyxdb.platform.generated.openapi.models.ResourceDTO;
import com.onyxdb.platform.mdb.quotas.QuotaProvider;

import static com.onyxdb.platform.generated.jooq.Tables.RESOURCES;

public class ResourceMapper {
    public ResourceDTO resourceToResourceDTO(Resource r) {
        return new ResourceDTO(
                r.id(),
                r.name(),
                r.description(),
                r.type().value(),
                r.type().getUnit().value()
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

    public Resource resourceRecordToResource(ResourcesRecord r) {
        return new Resource(
                r.getId(),
                r.getName(),
                r.getDescription(),
                ResourceType.fromValue(r.getType().getLiteral()),
                QuotaProvider.R.fromValue(r.getProvider().getLiteral())
        );
    }
}

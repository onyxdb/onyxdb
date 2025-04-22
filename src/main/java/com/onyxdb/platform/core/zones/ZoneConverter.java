package com.onyxdb.platform.core.zones;

import java.util.List;

import org.jooq.Record;

import com.onyxdb.platform.generated.jooq.tables.records.ZonesRecord;
import com.onyxdb.platform.generated.openapi.models.V1CreateZoneRequest;
import com.onyxdb.platform.generated.openapi.models.V1ListZonesResponse;
import com.onyxdb.platform.generated.openapi.models.V1UpdateZoneRequest;
import com.onyxdb.platform.generated.openapi.models.V1ZoneResponse;

/**
 * @author foxleren
 */
public final class ZoneConverter {
    public static Zone fromJooqRecord(Record r) {
        ZonesRecord rr = r.into(ZonesRecord.class);
        return new Zone(
                rr.getId(),
                rr.getDescription(),
                rr.getSelector()
        );
    }

    public static ZonesRecord toJooqZonesRecord(Zone z) {
        return new ZonesRecord(
                z.id(),
                z.description(),
                z.selector()
        );
    }

    public static V1ZoneResponse toV1ZoneResponse(Zone z) {
        return new V1ZoneResponse(
                z.id(),
                z.description(),
                z.selector()
        );
    }

    public static V1ListZonesResponse toV1ListZonesResponse(List<Zone> z) {
        return new V1ListZonesResponse(
                z.stream()
                        .map(ZoneConverter::toV1ZoneResponse)
                        .toList()
        );
    }

    public static Zone fromV1CreateZoneRequest(V1CreateZoneRequest r) {
        return new Zone(
                r.getId(),
                r.getDescription(),
                r.getSelector()
        );
    }

    public static Zone fromV1UpdateZoneRequest(String id, V1UpdateZoneRequest r) {
        return new Zone(
                id,
                r.getDescription(),
                r.getSelector()
        );
    }
}

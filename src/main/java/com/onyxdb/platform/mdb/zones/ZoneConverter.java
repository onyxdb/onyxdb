package com.onyxdb.platform.mdb.zones;

import org.jooq.Record;

import com.onyxdb.platform.generated.jooq.tables.records.ZonesRecord;

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
}

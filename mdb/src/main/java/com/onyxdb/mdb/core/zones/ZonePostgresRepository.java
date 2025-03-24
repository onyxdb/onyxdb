package com.onyxdb.mdb.core.zones;

import java.util.List;
import java.util.Optional;

import org.jooq.DSLContext;

import static com.onyxdb.mdb.generated.jooq.Tables.ZONES;

/**
 * @author foxleren
 */
public class ZonePostgresRepository implements ZoneRepository {
    private final DSLContext dslContext;

    public ZonePostgresRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public List<Zone> list() {
        return dslContext.select()
                .from(ZONES)
                .fetch()
                .map(ZoneConverter::fromJooqRecord);
    }

    @Override
    public Optional<Zone> getO(String id) {
        return dslContext.select()
                .from(ZONES)
                .where(ZONES.ID.eq(id))
                .fetchOptional()
                .map(ZoneConverter::fromJooqRecord);
    }

    @Override
    public void create(Zone zone) {
        dslContext.insertInto(ZONES)
                .set(ZoneConverter.toJooqZonesRecord(zone))
                .execute();
    }

    @Override
    public void update(Zone zone) {
        dslContext.update(ZONES)
                .set(ZoneConverter.toJooqZonesRecord(zone))
                .where(ZONES.ID.eq(zone.id()))
                .execute();
    }

    @Override
    public void delete(String id) {
        dslContext.deleteFrom(ZONES)
                .where(ZONES.ID.eq(id))
                .execute();
    }
}

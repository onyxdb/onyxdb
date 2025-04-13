package com.onyxdb.mdb.core.resourcePresets;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jooq.DSLContext;

import static com.onyxdb.mdb.generated.jooq.Tables.RESOURCE_PRESETS;

/**
 * @author foxleren
 */
public class ResourcePresetPostgresRepository implements ResourcePresetRepository {
    private final DSLContext dslContext;

    public ResourcePresetPostgresRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public List<ResourcePreset> list() {
        return dslContext.select()
                .from(RESOURCE_PRESETS)
                .fetch()
                .map(ResourcePresetConverter::fromJooqRecord);
    }

    @Override
    public Optional<ResourcePreset> getO(UUID id) {
        return dslContext.select()
                .from(RESOURCE_PRESETS)
                .where(RESOURCE_PRESETS.ID.eq(id))
                .fetchOptional()
                .map(ResourcePresetConverter::fromJooqRecord);
    }

    @Override
    public void create(ResourcePreset resourcePreset) {
        dslContext.insertInto(RESOURCE_PRESETS)
                .set(ResourcePresetConverter.toResourcePresetsRecord(resourcePreset))
                .execute();
    }

    @Override
    public void update(ResourcePreset resourcePreset) {
        dslContext.update(RESOURCE_PRESETS)
                .set(ResourcePresetConverter.toResourcePresetsRecord(resourcePreset))
                .where(RESOURCE_PRESETS.ID.eq(resourcePreset.id()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(RESOURCE_PRESETS)
                .where(RESOURCE_PRESETS.ID.eq(id))
                .execute();
    }
}

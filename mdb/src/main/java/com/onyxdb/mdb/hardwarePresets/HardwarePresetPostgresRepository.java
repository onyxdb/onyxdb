package com.onyxdb.mdb.hardwarePresets;

import java.util.List;
import java.util.Optional;

import org.jooq.DSLContext;

import static com.onyxdb.mdb.generated.jooq.Tables.HARDWARE_PRESETS;

/**
 * @author foxleren
 */
public class HardwarePresetPostgresRepository implements HardwarePresetRepository {
    private final DSLContext dslContext;

    public HardwarePresetPostgresRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public List<HardwarePreset> list() {
        return dslContext.select()
                .from(HARDWARE_PRESETS)
                .fetch()
                .map(HardwarePresetConverter::fromJooqRecord);
    }

    @Override
    public Optional<HardwarePreset> getO(String id) {
        return dslContext.select()
                .from(HARDWARE_PRESETS)
                .where(HARDWARE_PRESETS.ID.eq(id))
                .fetchOptional()
                .map(HardwarePresetConverter::fromJooqRecord);
    }

    @Override
    public void create(HardwarePreset hardwarePreset) {
        dslContext.insertInto(HARDWARE_PRESETS)
                .set(HardwarePresetConverter.toHardwarePresetsRecord(hardwarePreset))
                .execute();
    }

    @Override
    public void update(HardwarePreset hardwarePreset) {
        dslContext.update(HARDWARE_PRESETS)
                .set(HardwarePresetConverter.toHardwarePresetsRecord(hardwarePreset))
                .where(HARDWARE_PRESETS.ID.eq(hardwarePreset.id()))
                .execute();
    }

    @Override
    public void delete(String id) {
        dslContext.deleteFrom(HARDWARE_PRESETS)
                .where(HARDWARE_PRESETS.ID.eq(id))
                .execute();
    }
}

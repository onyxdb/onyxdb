package com.onyxdb.platform.mdb.resourcePresets;

import java.util.List;
import java.util.Optional;

import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.dao.DuplicateKeyException;

import com.onyxdb.platform.generated.jooq.Keys;
import com.onyxdb.platform.mdb.exceptions.ResourcePresetAlreadyExistsException;
import com.onyxdb.platform.mdb.utils.PsqlUtils;

import static com.onyxdb.platform.generated.jooq.Tables.RESOURCE_PRESETS;

/**
 * @author foxleren
 */
public class ResourcePresetPostgresRepository implements ResourcePresetRepository {
    private final DSLContext dslContext;
    private final ResourcePresetMapper resourcePresetMapper;

    public ResourcePresetPostgresRepository(DSLContext dslContext, ResourcePresetMapper resourcePresetMapper) {
        this.dslContext = dslContext;
        this.resourcePresetMapper = resourcePresetMapper;
    }

    @Override
    public List<ResourcePreset> list() {
        return dslContext.select()
                .from(RESOURCE_PRESETS)
                .fetch()
                .map(resourcePresetMapper::fromJooqRecord);
    }

    @Override
    public Optional<ResourcePreset> getO(String resourcePresetId) {
        return dslContext.select()
                .from(RESOURCE_PRESETS)
                .where(RESOURCE_PRESETS.ID.eq(resourcePresetId))
                .fetchOptional()
                .map(resourcePresetMapper::fromJooqRecord);
    }

    @Override
    public void create(ResourcePreset resourcePreset) {
        try {
            dslContext.insertInto(RESOURCE_PRESETS)
                    .set(resourcePresetMapper.toResourcePresetsRecord(resourcePreset))
                    .execute();
        } catch (DataAccessException | DuplicateKeyException e) {
            PsqlUtils.handleDataAccessEx(
                    e,
                    RESOURCE_PRESETS,
                    Keys.RESOURCE_PRESETS_PKEY,
                    () -> new ResourcePresetAlreadyExistsException(resourcePreset.id())
            );

            throw e;
        }
    }

    @Override
    public boolean update(ResourcePreset resourcePreset) {
        return dslContext.update(RESOURCE_PRESETS)
                .set(resourcePresetMapper.toResourcePresetsRecord(resourcePreset))
                .where(RESOURCE_PRESETS.ID.eq(resourcePreset.id()))
                .returning(RESOURCE_PRESETS.ID)
                .fetchOne() != null;
    }

    @Override
    public boolean delete(String resourcePresetId) {
        return dslContext.deleteFrom(RESOURCE_PRESETS)
                .where(RESOURCE_PRESETS.ID.eq(resourcePresetId))
                .returning(RESOURCE_PRESETS.ID)
                .fetchOne() != null;
    }
}

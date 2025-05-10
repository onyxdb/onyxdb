package com.onyxdb.platform.mdb.initialization;

import java.util.Objects;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.onyxdb.platform.mdb.utils.TimeUtils;

import static com.onyxdb.platform.generated.jooq.Tables.INITIALIZATION;

@Repository
public class InitializationPostgresRepository implements InitializationRepository {
    private final DSLContext dslContext;

    public InitializationPostgresRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public boolean getIsInitializedForUpdate() {
        return Objects.requireNonNull(dslContext.select(INITIALIZATION.IS_INITIALIZED)
                        .from(INITIALIZATION)
                        .forUpdate()
                        .fetchOne())
                .into(Boolean.class);
    }

    @Override
    public void markAsInitialized() {
        dslContext.update(INITIALIZATION)
                .set(INITIALIZATION.IS_INITIALIZED, true)
                .set(INITIALIZATION.INITIALIZED_AT, TimeUtils.now())
                .execute();
    }
}

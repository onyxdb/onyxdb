package com.onyxdb.mdb.repositories;

import java.util.UUID;

import org.jooq.DSLContext;

import com.onyxdb.mdb.models.Project;

import static com.onyxdb.mdb.generated.jooq.Tables.PROJECTS;

/**
 * @author foxleren
 */
public class ProjectPostgresRepository implements ProjectRepository {
    private final DSLContext dslContext;

    public ProjectPostgresRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public void create(Project project) {
//        dslContext.executeInsert(project.toJooqProjectRecord());
    }

    @Override
    public void delete(UUID id) {
        dslContext.delete(PROJECTS).where(PROJECTS.ID.eq(id)).execute();
    }
}

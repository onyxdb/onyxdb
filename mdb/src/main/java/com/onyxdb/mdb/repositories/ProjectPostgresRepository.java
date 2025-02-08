package com.onyxdb.mdb.repositories;

import org.jooq.DSLContext;

import com.onyxdb.mdb.models.Project;

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
        dslContext.executeInsert(project.toJooqProjectRecord());
    }
}

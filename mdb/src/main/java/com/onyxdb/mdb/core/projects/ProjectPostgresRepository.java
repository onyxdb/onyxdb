package com.onyxdb.mdb.core.projects;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jooq.DSLContext;

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
    public List<Project> list() {
        return dslContext.select()
                .from(PROJECTS)
                .fetch()
                .map(ProjectConverter::fromJooqRecord);
    }

    @Override
    public Optional<Project> getO(UUID id) {
        return dslContext.select()
                .from(PROJECTS)
                .where(PROJECTS.ID.eq(id))
                .fetchOptional()
                .map(ProjectConverter::fromJooqRecord);
    }

    @Override
    public void create(Project project) {
        dslContext.insertInto(PROJECTS)
                .set(ProjectConverter.toJooqProjectsRecord(project))
                .execute();
    }

    @Override
    public void update(UpdateProject updateProject) {
        dslContext.update(PROJECTS)
                .set(PROJECTS.NAME, updateProject.name())
                .set(PROJECTS.DESCRIPTION, updateProject.description())
                .where(PROJECTS.ID.eq(updateProject.id()))
                .execute();
    }

    @Override
    public void archive(UUID id) {
        setIsArchived(id, true);
    }

    @Override
    public void unarchive(UUID id) {
        setIsArchived(id, false);
    }

    private void setIsArchived(UUID id, boolean isArchived) {
        dslContext.update(PROJECTS)
                .set(PROJECTS.IS_ARCHIVED, isArchived)
                .where(PROJECTS.ID.eq(id))
                .execute();
    }
}

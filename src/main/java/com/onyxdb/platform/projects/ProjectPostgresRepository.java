package com.onyxdb.platform.projects;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;

import com.onyxdb.platform.exceptions.BadRequestException;
import com.onyxdb.platform.generated.jooq.Keys;
import com.onyxdb.platform.utils.PsqlUtils;

import static com.onyxdb.platform.generated.jooq.Tables.PROJECTS;

/**
 * @author foxleren
 */
public class ProjectPostgresRepository implements ProjectRepository {
    private final DSLContext dslContext;
    private final ProjectMapper projectMapper;

    public ProjectPostgresRepository(DSLContext dslContext, ProjectMapper projectMapper) {
        this.dslContext = dslContext;
        this.projectMapper = projectMapper;
    }

    @Override
    public List<Project> list() {
        return dslContext.select()
                .from(PROJECTS)
                .fetch()
                .map(ProjectMapper::jooqRecordToProject);
    }

    @Override
    public Optional<Project> getO(UUID id) {
        return dslContext.select()
                .from(PROJECTS)
                .where(PROJECTS.ID.eq(id))
                .fetchOptional()
                .map(ProjectMapper::jooqRecordToProject);
    }

    @Override
    public void create(ProjectToCreate projectToCreate) {
        Project project = projectMapper.projectToCreateToProject(projectToCreate);

        try {
            dslContext.insertInto(PROJECTS)
                    .set(ProjectMapper.toJooqProjectsRecord(project))
                    .execute();
        } catch (DataAccessException e) {
            PsqlUtils.handleDataAccessEx(
                    e,
                    PROJECTS,
                    Keys.PROJECTS_NAME_KEY,
                    () -> new BadRequestException("Project databaseName already exists")
            );

            throw e;
        }
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

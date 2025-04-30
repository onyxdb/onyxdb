package com.onyxdb.platform.mdb.projects;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.dao.DuplicateKeyException;

import com.onyxdb.platform.generated.jooq.Indexes;
import com.onyxdb.platform.mdb.exceptions.BadRequestException;
import com.onyxdb.platform.mdb.exceptions.ProjectNotFoundException;
import com.onyxdb.platform.mdb.utils.PsqlUtils;

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
    public Optional<Project> getO(UUID projectId) {
        return dslContext.select()
                .from(PROJECTS)
                .where(PROJECTS.ID.eq(projectId))
                .fetchOptional()
                .map(ProjectMapper::jooqRecordToProject);
    }

    @Override
    public Project get(UUID projectId) {
        return getO(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    @Override
    public void create(Project project) {
        try {
            dslContext.insertInto(PROJECTS)
                    .set(ProjectMapper.toJooqProjectsRecord(project))
                    .execute();
        } catch (DataAccessException | DuplicateKeyException e) {
            PsqlUtils.handleDataAccessEx(
                    e,
                    PROJECTS,
                    Indexes.PROJECT_NAME_IS_DELETED_UNIQ_IDX,
                    () -> new BadRequestException(
                            String.format("Project with name '%s' already exists", project.name())
                    )
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
                .set(PROJECTS.IS_DELETED, isArchived)
                .where(PROJECTS.ID.eq(id))
                .execute();
    }
}

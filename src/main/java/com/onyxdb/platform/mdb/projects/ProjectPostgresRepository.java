package com.onyxdb.platform.mdb.projects;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.dao.DuplicateKeyException;

import com.onyxdb.platform.generated.jooq.Indexes;
import com.onyxdb.platform.mdb.exceptions.ProjectAlreadyExistsException;
import com.onyxdb.platform.mdb.exceptions.ProjectNotFoundException;
import com.onyxdb.platform.mdb.utils.PsqlUtils;
import com.onyxdb.platform.mdb.utils.TimeUtils;

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
    public List<Project> listProjects() {
        return dslContext.select()
                .from(PROJECTS)
                .fetch()
                .map(ProjectMapper::jooqRecordToProject);
    }

    @Override
    public Optional<Project> getProjectO(UUID projectId, boolean isDeleted) {
        return dslContext.select()
                .from(PROJECTS)
                .where(PROJECTS.ID.eq(projectId).and(PROJECTS.IS_DELETED.eq(isDeleted)))
                .fetchOptional()
                .map(ProjectMapper::jooqRecordToProject);
    }

    @Override
    public Project getProjectOrThrow(UUID projectId, boolean isDeleted) {
        return getProjectO(projectId, isDeleted).orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    @Override
    public Project getProjectOrThrow(UUID projectId) {
        return getProjectOrThrow(projectId, false);
    }

    @Override
    public void createProject(Project project) {
        try {
            dslContext.insertInto(PROJECTS)
                    .set(ProjectMapper.toJooqProjectsRecord(project))
                    .execute();
        } catch (DataAccessException | DuplicateKeyException e) {
            PsqlUtils.handleDataAccessEx(
                    e,
                    PROJECTS,
                    Indexes.PROJECTS_PROJECT_NAME_IS_DELETED_UNIQ_IDX,
                    () -> new ProjectAlreadyExistsException(project.name())
            );

            throw e;
        }
    }

    @Override
    public boolean updateProject(UpdateProject updateProject) {
        var updatedProjectId = dslContext.update(PROJECTS)
                .set(PROJECTS.DESCRIPTION, updateProject.description())
                .where(PROJECTS.ID.eq(updateProject.id()))
                .returningResult(PROJECTS.ID)
                .fetchOne();

        return updatedProjectId != null;
    }

    @Override
    public boolean markAsDeleted(UUID projectId, UUID deletedBy) {
        var updatedProjectId = dslContext.update(PROJECTS)
                .set(PROJECTS.IS_DELETED, true)
                .set(PROJECTS.DELETED_AT, TimeUtils.now())
                .set(PROJECTS.DELETED_BY, deletedBy)
                .where(PROJECTS.ID.eq(projectId).and(PROJECTS.IS_DELETED.eq(false)))
                .returningResult(PROJECTS.ID)
                .fetchOne();

        return updatedProjectId != null;
    }
}

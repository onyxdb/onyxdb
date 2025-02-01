package com.onyxdb.idm.repositories;

import com.onyxdb.idm.generated.jooq.Tables;
import com.onyxdb.idm.generated.jooq.tables.ProjectTable;
import com.onyxdb.idm.models.Project;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author ArtemFed
 */
@Repository
@RequiredArgsConstructor
public class ProjectPostgresRepository implements ProjectRepository {
    private final DSLContext dslContext;
    private final static ProjectTable projectTable = Tables.PROJECT_TABLE;

    @Override
    public Optional<Project> findById(UUID id) {
        return dslContext.selectFrom(projectTable)
                .where(projectTable.ID.eq(id))
                .fetchOptional(Project::fromDAO);
    }

    @Override
    public List<Project> findAll() {
        return dslContext.selectFrom(projectTable)
                .fetch(Project::fromDAO);
    }

    @Override
    public void create(Project project) {
        dslContext.executeInsert(project.toDAO());
    }

    @Override
    public void update(Project project) {
        dslContext.update(projectTable)
                .set(projectTable.NAME, project.name())
                .set(projectTable.DESCRIPTION, project.description())
                .set(projectTable.UPDATED_AT, project.updatedAt())
                .set(projectTable.PARENT_ID, project.parent_id())
                .set(projectTable.OWNER_ID, project.ownerId())
                .where(projectTable.ID.eq(project.id()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dslContext.deleteFrom(projectTable)
                .where(projectTable.ID.eq(id))
                .execute();
    }
}
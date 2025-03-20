package com.onyxdb.mdb.repositories;

import java.util.UUID;

import com.onyxdb.mdb.models.Project;

/**
 * @author foxleren
 */
public interface ProjectRepository {
    void create(Project project);

    void delete(UUID id);
}

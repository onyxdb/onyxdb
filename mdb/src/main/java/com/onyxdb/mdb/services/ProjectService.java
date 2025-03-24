package com.onyxdb.mdb.services;

import com.onyxdb.mdb.repositories.ProjectRepository;

/**
 * @author foxleren
 */
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

//    public UUID create(ProjectToCreate projectToCreate) {
//        var project = Project.fromProjectToCreate(projectToCreate);
//        projectRepository.create(project);
//        return project.id();
//    }
}

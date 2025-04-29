package com.onyxdb.platform.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.ProjectsApi;
import com.onyxdb.platform.generated.openapi.models.CreateProjectRequestDTO;
import com.onyxdb.platform.generated.openapi.models.ListProjectsResponseDTO;
import com.onyxdb.platform.generated.openapi.models.ProjectDTO;
import com.onyxdb.platform.generated.openapi.models.UpdateProjectRequestDTO;
import com.onyxdb.platform.projects.Project;
import com.onyxdb.platform.projects.ProjectMapper;
import com.onyxdb.platform.projects.ProjectService;
import com.onyxdb.platform.projects.ProjectToCreate;
import com.onyxdb.platform.projects.UpdateProject;

/**
 * @author foxleren
 */
@RestController
public class ProjectController implements ProjectsApi {
    private final ProjectMapper projectMapper;
    private final ProjectService projectService;

    public ProjectController(
            ProjectMapper projectMapper,
            ProjectService projectService
    ) {
        this.projectMapper = projectMapper;
        this.projectService = projectService;
    }

    @Override
    public ResponseEntity<ListProjectsResponseDTO> listProjects() {
        List<Project> projects = projectService.list();
        List<ProjectDTO> projectDTOs = projects.stream().map(projectMapper::projectToProjectDTO).toList();

        return ResponseEntity.ok(new ListProjectsResponseDTO(
                projectDTOs
        ));
    }

    @Override
    public ResponseEntity<ProjectDTO> getProject(UUID projectId) {
        Project project = projectService.getOrThrow(projectId);
        var response = projectMapper.projectToProjectDTO(project);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<Void> createProject(CreateProjectRequestDTO rq) {
        ProjectToCreate projectToCreate = projectMapper.createProjectRequestDTOtoProjectToCreate(rq);
//        Project project = ProjectMapper.fromV1CreateProjectRequest(v1CreateProjectRequest);
//        projectService.create(project);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateProject(UUID projectId, UpdateProjectRequestDTO v1UpdateProjectRequest) {
        UpdateProject updateProject = ProjectMapper.fromV1UpdateProjectRequest(
                projectId,
                v1UpdateProjectRequest
        );
        projectService.update(updateProject);
        return ResponseEntity.ok().build();
    }
}

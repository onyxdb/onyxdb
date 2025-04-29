package com.onyxdb.platform.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.core.projects.Project;
import com.onyxdb.platform.core.projects.ProjectMapper;
import com.onyxdb.platform.core.projects.ProjectService;
import com.onyxdb.platform.core.projects.UpdateProject;
import com.onyxdb.platform.generated.openapi.apis.ProjectsApi;
import com.onyxdb.platform.generated.openapi.models.CreateProjectRequestDTO;
import com.onyxdb.platform.generated.openapi.models.ListProjectsResponseDTO;
import com.onyxdb.platform.generated.openapi.models.ProjectDTO;
import com.onyxdb.platform.generated.openapi.models.UpdateProjectRequestDTO;

/**
 * @author foxleren
 */
@RestController
public class ProjectController implements ProjectsApi {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public ResponseEntity<ListProjectsResponseDTO> listProjects() {
        List<Project> projects = projectService.list();
        var response = ProjectMapper.toV1ListProjectsResponse(projects);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<ProjectDTO> getProject(UUID projectId) {
        Project project = projectService.getOrThrow(projectId);
        var response = ProjectMapper.toV1ProjectResponse(project);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<Void> createProject(CreateProjectRequestDTO v1CreateProjectRequest) {
        Project project = ProjectMapper.fromV1CreateProjectRequest(v1CreateProjectRequest);
        projectService.create(project);
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

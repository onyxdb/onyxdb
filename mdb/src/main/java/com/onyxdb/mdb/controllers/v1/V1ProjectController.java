package com.onyxdb.mdb.controllers.v1;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.mdb.core.projects.Project;
import com.onyxdb.mdb.core.projects.ProjectConverter;
import com.onyxdb.mdb.core.projects.ProjectRepository;
import com.onyxdb.mdb.core.projects.ProjectService;
import com.onyxdb.mdb.core.projects.UpdateProject;
import com.onyxdb.mdb.generated.openapi.apis.V1ProjectsApi;
import com.onyxdb.mdb.generated.openapi.models.V1CreateProjectRequest;
import com.onyxdb.mdb.generated.openapi.models.V1ListProjectsResponse;
import com.onyxdb.mdb.generated.openapi.models.V1ProjectResponse;
import com.onyxdb.mdb.generated.openapi.models.V1UpdateProjectRequest;

/**
 * @author foxleren
 */
@RestController
public class V1ProjectController implements V1ProjectsApi {
    private final ProjectService projectService;
    private final ProjectRepository projectRepository;

    public V1ProjectController(ProjectService projectService, ProjectRepository projectRepository) {
        this.projectService = projectService;
        this.projectRepository = projectRepository;
    }

    @Override
    public ResponseEntity<V1ListProjectsResponse> listProjects() {
        List<Project> projects = projectService.list();
        var response = ProjectConverter.toV1ListProjectsResponse(projects);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<V1ProjectResponse> getProject(UUID projectId) {
        Project project = projectService.getOrThrow(projectId);
        var response = ProjectConverter.toV1ProjectResponse(project);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<Void> createProject(V1CreateProjectRequest v1CreateProjectRequest) {
        Project project = ProjectConverter.fromV1CreateProjectRequest(v1CreateProjectRequest);
        projectService.create(project);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateProject(UUID projectId, V1UpdateProjectRequest v1UpdateProjectRequest) {
        UpdateProject updateProject = ProjectConverter.fromV1UpdateProjectRequest(
                projectId,
                v1UpdateProjectRequest
        );
        projectService.update(updateProject);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> archiveProject(UUID projectId) {
        projectService.archive(projectId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> unarchiveProject(UUID projectId) {
        projectService.unarchive(projectId);
        return ResponseEntity.ok().build();
    }
}

package com.onyxdb.platform.mdb.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.ProjectsApi;
import com.onyxdb.platform.generated.openapi.models.CreateProjectRequestDTO;
import com.onyxdb.platform.generated.openapi.models.CreateProjectResponseDTO;
import com.onyxdb.platform.generated.openapi.models.ListProjectsResponseDTO;
import com.onyxdb.platform.generated.openapi.models.ProjectDTO;
import com.onyxdb.platform.generated.openapi.models.UpdateProjectRequestDTO;
import com.onyxdb.platform.mdb.projects.Project;
import com.onyxdb.platform.mdb.projects.ProjectMapper;
import com.onyxdb.platform.mdb.projects.ProjectService;
import com.onyxdb.platform.mdb.projects.ProjectToCreate;
import com.onyxdb.platform.mdb.projects.UpdateProject;

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
        return ResponseEntity.ok(projectMapper.projectToProjectDTO(project));
    }

    @Override
    public ResponseEntity<CreateProjectResponseDTO> createProject(CreateProjectRequestDTO rq) {
        ProjectToCreate projectToCreate = projectMapper.createProjectRequestDTOtoProjectToCreate(rq);
        UUID projectId = projectService.create(projectToCreate);

        return ResponseEntity.ok(new CreateProjectResponseDTO(
                projectId
        ));
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

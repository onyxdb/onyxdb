package com.onyxdb.idm.controllers.v1;

import com.onyxdb.idm.generated.openapi.apis.ProjectsApi;
import com.onyxdb.idm.generated.openapi.models.ProjectDTO;
import com.onyxdb.idm.generated.openapi.models.ServiceDTO;
import com.onyxdb.idm.models.Project;
import com.onyxdb.idm.models.Service;
import com.onyxdb.idm.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * @author ArtemFed
 */
@RestController
@RequiredArgsConstructor
public class ProjectsController implements ProjectsApi {
    private final ProjectService projectService;

    @Override
    public ResponseEntity<ProjectDTO> createProject(@Valid ProjectDTO projectDTO) {
        Project project = Project.fromDTO(projectDTO);
        Project createdProject = projectService.create(project);
        return new ResponseEntity<>(createdProject.toDTO(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteProject(UUID projectId) {
        projectService.delete(projectId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ProjectDTO> getProjectById(UUID projectId) {
        Project project = projectService.findById(projectId);
        return ResponseEntity.ok(project.toDTO());
    }

    @Override
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<Project> projects = projectService.findAll();
        List<ProjectDTO> projectDTOs = projects.stream().map(Project::toDTO).toList();
        return ResponseEntity.ok(projectDTOs);
    }

    @Override
    public ResponseEntity<List<ServiceDTO>> getServicesByProjectId(UUID projectId) {
        List<Service> services = projectService.getServicesByProjectId(projectId);
        List<ServiceDTO> serviceDTOs = services.stream().map(Service::toDTO).toList();
        return ResponseEntity.ok(serviceDTOs);
    }

    @Override
    public ResponseEntity<ProjectDTO> updateProject(UUID projectId, @Valid ProjectDTO projectDTO) {
        projectDTO.setId(projectId);
        Project project = Project.fromDTO(projectDTO);
        Project updatedProject = projectService.update(project);
        return ResponseEntity.ok(updatedProject.toDTO());
    }
}

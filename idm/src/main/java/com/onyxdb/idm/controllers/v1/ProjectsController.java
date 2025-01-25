//package com.onyxdb.idm.controllers.v1;
//
//import com.onyxdb.idm.generated.openapi.apis.ProjectsApi;
//import com.onyxdb.idm.generated.openapi.models.ProjectDTO;
//import com.onyxdb.idm.generated.openapi.models.BadRequestResponse;
//import com.onyxdb.idm.generated.openapi.models.NotFoundResponse;
//import com.onyxdb.idm.models.Project;
//import com.onyxdb.idm.services.ProjectService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.validation.Valid;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@RestController
//@RequiredArgsConstructor
//public class ProjectsController implements ProjectsApi {
//
//    private final ProjectService projectService;
//
//    @Override
//    public ResponseEntity<ProjectDTO> createProject(@Valid ProjectDTO projectDTO) {
//        Project project = Project.fromDTO(projectDTO);
//        projectService.create(project);
//        return new ResponseEntity<>(project.toDTO(), HttpStatus.CREATED);
//    }
//
//    @Override
//    public ResponseEntity<Void> deleteProject(UUID projectId) {
//        Optional<Project> project = projectService.findById(projectId);
//        if (project.isPresent()) {
//            projectService.delete(projectId);
//            return ResponseEntity.noContent().build();
//        } else {
//            NotFoundResponse notFoundResponse = new NotFoundResponse();
//            notFoundResponse.setMessage("Project not found");
//            return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @Override
//    public ResponseEntity<ProjectDTO> getProjectById(UUID projectId) {
//        Optional<Project> project = projectService.findById(projectId);
//        if (project.isPresent()) {
//            return ResponseEntity.ok(project.get().toDTO());
//        } else {
//            NotFoundResponse notFoundResponse = new NotFoundResponse();
//            notFoundResponse.setMessage("Project not found");
//            return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @Override
//    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
//        List<Project> projects = projectService.findAll();
//        List<ProjectDTO> projectDTOs = projects.stream().map(Project::toDTO).toList();
//        return ResponseEntity.ok(projectDTOs);
//    }
//
//    @Override
//    public ResponseEntity<ProjectDTO> updateProject(UUID projectId, @Valid ProjectDTO projectDTO) {
//        Optional<Project> existingProject = projectService.findById(projectId);
//        if (existingProject.isPresent()) {
//            Project project = Project.fromDTO(projectDTO);
//            projectService.update(project);
//            return ResponseEntity.ok(project.toDTO());
//        } else {
//            NotFoundResponse notFoundResponse = new NotFoundResponse();
//            notFoundResponse.setMessage("Project not found");
//            return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
//        }
//    }
//}

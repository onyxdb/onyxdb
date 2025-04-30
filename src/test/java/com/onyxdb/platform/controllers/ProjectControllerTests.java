package com.onyxdb.platform.controllers;

import java.util.List;
import java.util.UUID;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.onyxdb.platform.BaseTest;
import com.onyxdb.platform.generated.openapi.models.BadRequestResponse;
import com.onyxdb.platform.generated.openapi.models.CreateProjectRequestDTO;
import com.onyxdb.platform.generated.openapi.models.CreateProjectResponseDTO;
import com.onyxdb.platform.generated.openapi.models.ListProjectsResponseDTO;
import com.onyxdb.platform.generated.openapi.models.ProjectDTO;
import com.onyxdb.platform.mdb.projects.Project;
import com.onyxdb.platform.mdb.projects.ProjectMapper;
import com.onyxdb.platform.mdb.projects.ProjectRepository;

import static org.hamcrest.CoreMatchers.is;

public class ProjectControllerTests extends BaseTest {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectMapper projectMapper;

    @BeforeEach
    public void setUp() {
        truncateTables();
    }

    @Test
    public void listProjects() {
        var project1 = Project.create(
                "project1",
                "project1 desc",
                UUID.randomUUID()
        );
        var project2 = Project.create(
                "project2",
                "project2 desc",
                UUID.randomUUID()
        );

        projectRepository.create(project1);
        projectRepository.create(project2);

        var expected = new ListProjectsResponseDTO(List.of(
                projectMapper.projectToProjectDTO(project1),
                projectMapper.projectToProjectDTO(project2))
        );

        ResponseEntity<ListProjectsResponseDTO> response = restTemplate.getForEntity(
                "/api/projects",
                ListProjectsResponseDTO.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        MatcherAssert.assertThat(response.getBody(), is(expected));
    }

    @Test
    public void getProject() {
        var project = Project.create(
                "project",
                "project desc",
                UUID.randomUUID()
        );

        projectRepository.create(project);

        ResponseEntity<ProjectDTO> response = restTemplate.getForEntity(
                "/api/projects/{projectId}",
                ProjectDTO.class,
                project.id()
        );

        var expected = projectMapper.projectToProjectDTO(project);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        MatcherAssert.assertThat(response.getBody(), is(expected));
    }

    @Test
    public void whenProjectNotFound_then404() {
        var projectId = UUID.randomUUID();

        ResponseEntity<BadRequestResponse> response = restTemplate.getForEntity(
                "/api/projects/{projectId}",
                BadRequestResponse.class,
                projectId
        );

        var expected = new BadRequestResponse(String.format("Project with id '%s' is not found", projectId));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        MatcherAssert.assertThat(response.getBody(), is(expected));
    }

    @Test
    public void createProject() {
        var rq = new CreateProjectRequestDTO(
                "project",
                "project description",
                UUID.randomUUID()
        );

        ResponseEntity<CreateProjectResponseDTO> createResponse = restTemplate.postForEntity(
                "/api/projects",
                rq,
                CreateProjectResponseDTO.class
        );
        Assertions.assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        Assertions.assertNotNull(createResponse.getBody());

        UUID projectId = createResponse.getBody().getProjectId();
        Assertions.assertNotNull(projectId);

        ResponseEntity<ProjectDTO> getResponse = restTemplate.getForEntity(
                "/api/projects/{projectId}",
                ProjectDTO.class,
                projectId
        );

        var expected = new ProjectDTO(
                projectId,
                rq.getName(),
                rq.getDescription(),
                rq.getProductId()
        );

        MatcherAssert.assertThat(getResponse.getBody(), is(expected));
    }

    @Test
    public void whenCreateProjectNameDuplicate_then400() {
        var project = Project.create(
                "project",
                "project desc",
                UUID.randomUUID()
        );
        projectRepository.create(project);

        var rq = new CreateProjectRequestDTO(
                project.name(),
                "duplicate project desc",
                project.productId()
        );

        ResponseEntity<BadRequestResponse> response = restTemplate.postForEntity(
                "/api/projects",
                rq,
                BadRequestResponse.class
        );

        var expected = new BadRequestResponse(String.format("Project with name '%s' already exists", project.name()));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        MatcherAssert.assertThat(response.getBody(), is(expected));
    }
}

package com.onyxdb.platform.controllers;

import java.util.List;
import java.util.UUID;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.onyxdb.platform.BaseTest;
import com.onyxdb.platform.TestUtils;
import com.onyxdb.platform.generated.openapi.models.BadRequestResponse;
import com.onyxdb.platform.generated.openapi.models.CreateProjectRequestDTO;
import com.onyxdb.platform.generated.openapi.models.CreateProjectResponseDTO;
import com.onyxdb.platform.generated.openapi.models.ListProjectsResponseDTO;
import com.onyxdb.platform.generated.openapi.models.ProjectDTO;
import com.onyxdb.platform.generated.openapi.models.UpdateProjectRequestDTO;
import com.onyxdb.platform.mdb.exceptions.ProjectAlreadyExistsException;
import com.onyxdb.platform.mdb.exceptions.ProjectNotFoundException;
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
                TestUtils.productId1
        );
        var project2 = Project.create(
                "project2",
                "project2 desc",
                TestUtils.productId2
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
                TestUtils.productId1
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
        var projectId = TestUtils.projectId1;

        ResponseEntity<BadRequestResponse> response = restTemplate.getForEntity(
                "/api/projects/{projectId}",
                BadRequestResponse.class,
                projectId
        );

        var expected = new BadRequestResponse(ProjectNotFoundException.buildMessage(projectId));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        MatcherAssert.assertThat(response.getBody(), is(expected));
    }

    @Test
    public void createProject() {
        var rq = new CreateProjectRequestDTO(
                "project",
                "project description",
                TestUtils.productId1
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

        Project project = projectRepository.getOrThrow(projectId);

        var expected = new Project(
                projectId,
                rq.getName(),
                rq.getDescription(),
                rq.getProductId(),
                false
        );

        MatcherAssert.assertThat(project, is(expected));
    }

    @Test
    public void whenCreateProjectNameDuplicate_then400() {
        var project = Project.create(
                "project",
                "project desc",
                TestUtils.productId1
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

        var expected = new BadRequestResponse(ProjectAlreadyExistsException.buildMessage(project.name()));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        MatcherAssert.assertThat(response.getBody(), is(expected));
    }

    @Test
    public void updateProject() {
        var projectBefore = Project.create(
                "name before",
                "desc before",
                TestUtils.productId1
        );
        projectRepository.create(projectBefore);

        var rq = new UpdateProjectRequestDTO(
                "updated name",
                "updated desc",
                TestUtils.productId2
        );

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/projects/{projectId}",
                HttpMethod.PUT,
                new HttpEntity<>(rq),
                Void.class,
                projectBefore.id()
        );

        Project updatedProject = projectRepository.getOrThrow(projectBefore.id());

        var expected = new Project(
                projectBefore.id(),
                rq.getName(),
                rq.getDescription(),
                rq.getProductId(),
                false
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        MatcherAssert.assertThat(updatedProject, is(expected));
    }

    @Test
    public void whenUpdateProjectNotExistingProject_then404() {
        var projectId = TestUtils.projectId1;

        var rq = new UpdateProjectRequestDTO(
                "updated name",
                "updated desc",
                TestUtils.productId1
        );

        ResponseEntity<BadRequestResponse> response = restTemplate.exchange(
                "/api/projects/{projectId}",
                HttpMethod.PUT,
                new HttpEntity<>(rq),
                BadRequestResponse.class,
                projectId
        );

        var expected = new BadRequestResponse(ProjectNotFoundException.buildMessage(projectId));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        MatcherAssert.assertThat(response.getBody(), is(expected));
    }

    @Test
    public void whenUpdateProjectNameDuplicate_then400() {
        var project1 = Project.create(
                "project1",
                "desc1",
                TestUtils.productId1
        );
        var project2 = Project.create(
                "project2",
                "desc2",
                TestUtils.productId2
        );
        projectRepository.create(project1);
        projectRepository.create(project2);

        var rq = new UpdateProjectRequestDTO(
                project1.name(),
                "updated desc",
                TestUtils.productId2
        );

        ResponseEntity<BadRequestResponse> response = restTemplate.exchange(
                "/api/projects/{projectId}",
                HttpMethod.PUT,
                new HttpEntity<>(rq),
                BadRequestResponse.class,
                project2.id()
        );

        var expected = new BadRequestResponse(ProjectAlreadyExistsException.buildMessage(rq.getName()));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        MatcherAssert.assertThat(response.getBody(), is(expected));
    }
}

package com.onyxdb.platform.controllers;

import java.util.List;
import java.util.UUID;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class ProjectControllerTests extends BaseTest {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectMapper projectMapper;

    @BeforeEach
    public void beforeEach() {
        truncateTables();
    }

    @Test
    public void listProjects() {
        var project1 = Project.create(
                "project1",
                "project1 desc",
                TestUtils.PARENT_PRODUCT_ID,
                TestUtils.DEFAULT_NAMESPACE,
                TestUtils.ADMIN_ID
        );
        var project2 = Project.create(
                "project2",
                "project2 desc",
                TestUtils.CHILD_PRODUCT_ID,
                TestUtils.DEFAULT_NAMESPACE,
                TestUtils.ADMIN_ID
        );

        projectRepository.createProject(project1);
        projectRepository.createProject(project2);

        var expected = new ListProjectsResponseDTO(List.of(
                projectMapper.projectToProjectDTO(project1),
                projectMapper.projectToProjectDTO(project2))
        );

        ResponseEntity<ListProjectsResponseDTO> response = restTemplate.exchange(
                "/api/mdb/projects",
                HttpMethod.GET,
                new HttpEntity<>(getHeaders()),
                ListProjectsResponseDTO.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().getProjects())
                .usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(expected.getProjects());
    }

    @Test
    public void getProject() {
        var project = Project.create(
                "project",
                "project desc",
                TestUtils.PARENT_PRODUCT_ID,
                TestUtils.DEFAULT_NAMESPACE,
                TestUtils.ADMIN_ID
        );

        projectRepository.createProject(project);

        ResponseEntity<ProjectDTO> response = restTemplate.exchange(
                "/api/mdb/projects/{projectId}",
                HttpMethod.GET,
                new HttpEntity<>(getHeaders()),
                ProjectDTO.class,
                project.id()
        );

        var expected = projectMapper.projectToProjectDTO(project);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody())
                .usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(expected);
    }

    @Test
    public void whenProjectNotFound_then404() {
        var projectId = TestUtils.SANDBOX_PROJECT_ID;

        ResponseEntity<BadRequestResponse> response = restTemplate.exchange(
                "/api/mdb/projects/{projectId}",
                HttpMethod.GET,
                new HttpEntity<>(getHeaders()),
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
                TestUtils.PARENT_PRODUCT_ID,
                TestUtils.DEFAULT_NAMESPACE
        );

        ResponseEntity<CreateProjectResponseDTO> createResponse = restTemplate.postForEntity(
                "/api/mdb/projects",
                new HttpEntity<>(rq, getHeaders()),
                CreateProjectResponseDTO.class
        );
        Assertions.assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        Assertions.assertNotNull(createResponse.getBody());

        UUID projectId = createResponse.getBody().getProjectId();
        Assertions.assertNotNull(projectId);

        Project project = projectRepository.getProjectOrThrow(projectId, false);

        var expected = Project.create(
                projectId,
                rq.getName(),
                rq.getDescription(),
                rq.getProductId(),
                TestUtils.DEFAULT_NAMESPACE,
                TestUtils.ADMIN_ID
        );

        assertThat(project)
                .usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(expected);
    }

    @Test
    public void whenCreateProjectNameDuplicate_then400() {
        var project = Project.create(
                "project",
                "project desc",
                TestUtils.PARENT_PRODUCT_ID,
                TestUtils.DEFAULT_NAMESPACE,
                TestUtils.ADMIN_ID
        );
        projectRepository.createProject(project);

        var rq = new CreateProjectRequestDTO(
                project.name(),
                "duplicate project desc",
                project.productId(),
                TestUtils.DEFAULT_NAMESPACE
        );

        ResponseEntity<BadRequestResponse> response = restTemplate.exchange(
                "/api/mdb/projects",
                HttpMethod.POST,
                new HttpEntity<>(rq, getHeaders()),
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
                TestUtils.PARENT_PRODUCT_ID,
                TestUtils.DEFAULT_NAMESPACE,
                TestUtils.ADMIN_ID
        );
        projectRepository.createProject(projectBefore);

        var rq = new UpdateProjectRequestDTO(
                "updated desc"
        );

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/mdb/projects/{projectId}",
                HttpMethod.PUT,
                new HttpEntity<>(rq, getHeaders()),
                Void.class,
                projectBefore.id()
        );

        Project updatedProject = projectRepository.getProjectOrThrow(projectBefore.id(), false);

        var expected = Project.create(
                projectBefore.id(),
                projectBefore.name(),
                rq.getDescription(),
                projectBefore.productId(),
                TestUtils.DEFAULT_NAMESPACE,
                TestUtils.ADMIN_ID
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(updatedProject)
                .usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(expected);
    }

    @Test
    public void whenUpdateProjectNotExistingProject_then404() {
        var projectId = TestUtils.SANDBOX_PROJECT_ID;

        var rq = new UpdateProjectRequestDTO(
                "updated desc"
        );

        ResponseEntity<BadRequestResponse> response = restTemplate.exchange(
                "/api/mdb/projects/{projectId}",
                HttpMethod.PUT,
                new HttpEntity<>(rq, getHeaders()),
                BadRequestResponse.class,
                projectId
        );

        var expected = new BadRequestResponse(ProjectNotFoundException.buildMessage(projectId));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        MatcherAssert.assertThat(response.getBody(), is(expected));
    }

    @Test
    @Disabled
    public void whenUpdateProjectNameDuplicate_then400() {
        var project1 = Project.create(
                "project1",
                "desc1",
                TestUtils.PARENT_PRODUCT_ID,
                TestUtils.DEFAULT_NAMESPACE,
                TestUtils.ADMIN_ID
        );
        var project2 = Project.create(
                "project2",
                "desc2",
                TestUtils.CHILD_PRODUCT_ID,
                TestUtils.DEFAULT_NAMESPACE,
                TestUtils.ADMIN_ID
        );
        projectRepository.createProject(project1);
        projectRepository.createProject(project2);

        var rq = new UpdateProjectRequestDTO(
                "updated desc"
        );

        ResponseEntity<BadRequestResponse> response = restTemplate.exchange(
                "/api/mdb/projects/{projectId}",
                HttpMethod.PUT,
                new HttpEntity<>(rq, getHeaders()),
                BadRequestResponse.class,
                project2.id()
        );

//        var expected = new BadRequestResponse(ProjectAlreadyExistsException.buildMessage(rq.getName()));
//
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        Assertions.assertNotNull(response.getBody());
//        MatcherAssert.assertThat(response.getBody(), is(expected));
    }

    @Test
    public void deleteProject() {
        var project = Project.create(
                "name",
                "desc",
                TestUtils.PARENT_PRODUCT_ID,
                TestUtils.DEFAULT_NAMESPACE,
                TestUtils.ADMIN_ID
        );
        projectRepository.createProject(project);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/mdb/projects/{projectId}",
                HttpMethod.DELETE,
                new HttpEntity<>(getHeaders()),
                Void.class,
                project.id()
        );

        var deletedProject = projectRepository.getProjectOrThrow(project.id(), true);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(deletedProject.isDeleted());
        Assertions.assertNotNull(deletedProject.deletedAt());
        Assertions.assertEquals(TestUtils.ADMIN_ID, deletedProject.deletedBy());
    }

    @Test
    public void whenDeleteNotExistingProject_then404() {
        var projectId = TestUtils.SANDBOX_PROJECT_ID;
        ResponseEntity<BadRequestResponse> response = restTemplate.exchange(
                "/api/mdb/projects/{projectId}",
                HttpMethod.DELETE,
                new HttpEntity<>(getHeaders()),
                BadRequestResponse.class,
                projectId
        );

        var expected = new BadRequestResponse(ProjectNotFoundException.buildMessage(projectId));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        MatcherAssert.assertThat(response.getBody(), is(expected));
    }

    @Test
    public void whenDeleteAlreadyDeletedProject_then400() {
        var project = Project.create(
                "name",
                "desc",
                TestUtils.PARENT_PRODUCT_ID,
                TestUtils.DEFAULT_NAMESPACE,
                TestUtils.ADMIN_ID
        );
        projectRepository.createProject(project);

        restTemplate.exchange(
                "/api/mdb/projects/{projectId}",
                HttpMethod.DELETE,
                new HttpEntity<>(getHeaders()),
                Void.class,
                project.id()
        );

        ResponseEntity<BadRequestResponse> response2 = restTemplate.exchange(
                "/api/mdb/projects/{projectId}",
                HttpMethod.DELETE,
                new HttpEntity<>(getHeaders()),
                BadRequestResponse.class,
                project.id()
        );

        var expected = new BadRequestResponse(ProjectNotFoundException.buildMessage(project.id()));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
        Assertions.assertNotNull(response2.getBody());
        MatcherAssert.assertThat(response2.getBody(), is(expected));
    }
}

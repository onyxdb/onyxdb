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
import com.onyxdb.platform.generated.openapi.models.ListProjectsResponseDTO;
import com.onyxdb.platform.generated.openapi.models.ProjectDTO;
import com.onyxdb.platform.projects.ProjectMapper;
import com.onyxdb.platform.projects.ProjectRepository;
import com.onyxdb.platform.projects.ProjectToCreate;

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
        var projectToCreate1 = new ProjectToCreate(
                UUID.randomUUID(),
                "project1",
                "project1 desc",
                UUID.randomUUID()
        );
        var projectToCreate2 = new ProjectToCreate(
                UUID.randomUUID(),
                "project2",
                "project2 desc",
                UUID.randomUUID()
        );

        projectRepository.create(projectToCreate1);
        projectRepository.create(projectToCreate2);

        var expected = new ListProjectsResponseDTO(List.of(
                projectMapper.projectToProjectDTO(projectMapper.projectToCreateToProject(projectToCreate1)),
                projectMapper.projectToProjectDTO(projectMapper.projectToCreateToProject(projectToCreate2))
        ));

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
        var projectToCreate = new ProjectToCreate(
                UUID.randomUUID(),
                "project",
                "project desc",
                UUID.randomUUID()
        );

        projectRepository.create(projectToCreate);

        ResponseEntity<ProjectDTO> response = restTemplate.getForEntity(
                "/api/projects/{projectId}",
                ProjectDTO.class,
                projectToCreate.id()
        );

        var expected = projectMapper.projectToProjectDTO(projectMapper.projectToCreateToProject(projectToCreate));

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
}

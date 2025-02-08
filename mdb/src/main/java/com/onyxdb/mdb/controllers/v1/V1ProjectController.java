package com.onyxdb.mdb.controllers.v1;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.mdb.generated.openapi.apis.V1ProjectsApi;
import com.onyxdb.mdb.generated.openapi.models.V1CreateProjectRequest;
import com.onyxdb.mdb.generated.openapi.models.V1CreateProjectResponse;
import com.onyxdb.mdb.models.ProjectToCreate;
import com.onyxdb.mdb.services.ProjectService;

/**
 * @author foxleren
 */
@RestController
public class V1ProjectController implements V1ProjectsApi {
    private final ProjectService projectService;

    public V1ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public ResponseEntity<V1CreateProjectResponse> v1ProjectsCreateProject(V1CreateProjectRequest rq) {
        UUID projectId = projectService.create(ProjectToCreate.fromV1CreateProjectRequest(rq));
        var response = new V1CreateProjectResponse(projectId);
        return ResponseEntity.ok(response);
    }
}

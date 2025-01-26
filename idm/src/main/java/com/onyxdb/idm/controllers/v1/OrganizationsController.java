package com.onyxdb.idm.controllers.v1;

import com.onyxdb.idm.generated.openapi.apis.OrganizationsApi;
import com.onyxdb.idm.generated.openapi.models.OrganizationDTO;
import com.onyxdb.idm.models.Organization;
import com.onyxdb.idm.services.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrganizationsController implements OrganizationsApi {
    private final OrganizationService organizationService;


    /**
     * POST /api/v1/organizations : Create a new organization
     *
     * @param organizationDTO (required)
     * @return Created (status code 201)
     * or Bad Request (status code 400)
     */
    @Override
    public ResponseEntity<OrganizationDTO> createOrganization(OrganizationDTO organizationDTO) {
        Organization organization = Organization.fromDTO(organizationDTO);
        Organization createdOrganization = organizationService.create(organization);
        return new ResponseEntity<>(createdOrganization.toDTO(), HttpStatus.CREATED);
    }

    /**
     * DELETE /api/v1/organizations/{organizationId} : Delete an organization by ID
     *
     * @param organizationId (required)
     * @return No Content (status code 204)
     * or Not Found (status code 404)
     * or Bad Request (status code 400)
     */
    @Override
    public ResponseEntity<Void> deleteOrganization(UUID organizationId) {
        organizationService.delete(organizationId);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/v1/organizations : Get all organizations
     *
     * @return OK (status code 200)
     * or Bad Request (status code 400)
     */
    @Override
    public ResponseEntity<List<OrganizationDTO>> getAllOrganizations() {
        List<Organization> organizations = organizationService.findAll();
        List<OrganizationDTO> organizationDTOs = organizations.stream().map(Organization::toDTO).toList();
        return ResponseEntity.ok(organizationDTOs);
    }

    /**
     * GET /api/v1/organizations/{organizationId} : Get an organization by ID
     *
     * @param organizationId (required)
     * @return OK (status code 200)
     * or Not Found (status code 404)
     * or Bad Request (status code 400)
     */
    @Override
    public ResponseEntity<OrganizationDTO> getOrganizationById(UUID organizationId) {
        Organization organization = organizationService.findById(organizationId);
        return ResponseEntity.ok(organization.toDTO());
    }

    /**
     * PUT /api/v1/organizations/{organizationId} : Update an organization by ID
     *
     * @param organizationId  (required)
     * @param organizationDTO (required)
     * @return OK (status code 200)
     * or Not Found (status code 404)
     * or Bad Request (status code 400)
     */
    @Override
    public ResponseEntity<OrganizationDTO> updateOrganization(UUID organizationId, OrganizationDTO organizationDTO) {
        organizationDTO.setId(organizationId);
        Organization organization = Organization.fromDTO(organizationDTO);
        Organization updatedOrganization = organizationService.update(organization);
        return ResponseEntity.ok(updatedOrganization.toDTO());
    }
}

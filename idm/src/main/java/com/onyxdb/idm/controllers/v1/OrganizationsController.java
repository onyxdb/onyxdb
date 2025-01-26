//package com.onyxdb.idm.controllers.v1;
//
//import com.onyxdb.idm.generated.openapi.apis.OrganizationsApi;
//import com.onyxdb.idm.generated.openapi.models.OrganizationDTO;
//import com.onyxdb.idm.generated.openapi.models.BadRequestResponse;
//import com.onyxdb.idm.generated.openapi.models.NotFoundResponse;
//import com.onyxdb.idm.models.Organization;
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
//public class OrganizationsController implements OrganizationsApi {
//
//    private final OrganizationService organizationService;
//
//    @Override
//    public ResponseEntity<OrganizationDTO> createOrganization(@Valid OrganizationDTO organizationDTO) {
//        Organization organization = Organization.fromDTO(organizationDTO);
//        organizationService.create(organization);
//        return new ResponseEntity<>(organization.toDTO(), HttpStatus.CREATED);
//    }
//
//    @Override
//    public ResponseEntity<Void> deleteOrganization(UUID organizationId) {
//        Optional<Organization> organization = organizationService.findById(organizationId);
//        if (organization.isPresent()) {
//            organizationService.delete(organizationId);
//            return ResponseEntity.noContent().build();
//        } else {
//            NotFoundResponse notFoundResponse = new NotFoundResponse();
//            notFoundResponse.setMessage("Organization not found");
//            return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @Override
//    public ResponseEntity<OrganizationDTO> getOrganizationById(UUID organizationId) {
//        Optional<Organization> organization = organizationService.findById(organizationId);
//        if (organization.isPresent()) {
//            return ResponseEntity.ok(organization.get().toDTO());
//        } else {
//            NotFoundResponse notFoundResponse = new NotFoundResponse();
//            notFoundResponse.setMessage("Organization not found");
//            return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @Override
//    public ResponseEntity<List<OrganizationDTO>> getAllOrganizations() {
//        List<Organization> organizations = organizationService.findAll();
//        List<OrganizationDTO> organizationDTOs = organizations.stream().map(Organization::toDTO).toList();
//        return ResponseEntity.ok(organizationDTOs);
//    }
//
//    @Override
//    public ResponseEntity<OrganizationDTO> updateOrganization(UUID organizationId, @Valid OrganizationDTO organizationDTO) {
//        Optional<Organization> existingOrganization = organizationService.findById(organizationId);
//        if (existingOrganization.isPresent()) {
//            Organization organization = Organization.fromDTO(organizationDTO);
//            organizationService.update(organization);
//            return ResponseEntity.ok(organization.toDTO());
//        } else {
//            NotFoundResponse notFoundResponse = new NotFoundResponse();
//            notFoundResponse.setMessage("Organization not found");
//            return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
//        }
//    }
//}
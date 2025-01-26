//package com.onyxdb.idm.controllers.v1;
//
//import com.onyxdb.idm.generated.openapi.apis.ResourcesApi;
//import com.onyxdb.idm.generated.openapi.models.ResourceDTO;
//import com.onyxdb.idm.models.Resource;
//import com.onyxdb.idm.services.ResourceService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.validation.Valid;
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequiredArgsConstructor
//public class ResourcesController implements ResourcesApi {
//    private final ResourceService resourceService;
//
//    @Override
//    public ResponseEntity<ResourceDTO> createResource(@Valid ResourceDTO resourceDTO) {
//        Resource resource = Resource.fromDTO(resourceDTO);
//        Resource createdResource = resourceService.create(resource);
//        return new ResponseEntity<>(createdResource.toDTO(), HttpStatus.CREATED);
//    }
//
//    @Override
//    public ResponseEntity<Void> deleteResource(UUID resourceId) {
//        resourceService.delete(resourceId);
//        return ResponseEntity.noContent().build();
//    }
//
//    @Override
//    public ResponseEntity<ResourceDTO> getResourceById(UUID resourceId) {
//        Resource resource = resourceService.findById(resourceId);
//        return ResponseEntity.ok(resource.toDTO());
//    }
//
//    @Override
//    public ResponseEntity<List<ResourceDTO>> getAllResources() {
//        List<Resource> resources = resourceService.findAll();
//        List<ResourceDTO> resourceDTOs = resources.stream().map(Resource::toDTO).toList();
//        return ResponseEntity.ok(resourceDTOs);
//    }
//
//    @Override
//    public ResponseEntity<ResourceDTO> updateResource(UUID resourceId, @Valid ResourceDTO resourceDTO) {
//        Resource resource = Resource.fromDTO(resourceDTO);
//        Resource updatedResource = resourceService.update(resource);
//        return ResponseEntity.ok(updatedResource.toDTO());
//    }
//}
package com.onyxdb.idm.controllers.v1;

import com.onyxdb.idm.generated.openapi.apis.ServicesApi;
import com.onyxdb.idm.generated.openapi.models.ServiceDTO;
import com.onyxdb.idm.models.Service;
import com.onyxdb.idm.services.ServiceService;
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
public class ServicesController implements ServicesApi {
    private final ServiceService serviceService;

    @Override
    public ResponseEntity<ServiceDTO> createService(@Valid ServiceDTO serviceDTO) {
        Service service = Service.fromDTO(serviceDTO);
        Service createdService = serviceService.create(service);
        return new ResponseEntity<>(createdService.toDTO(), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteService(UUID serviceId) {
        serviceService.delete(serviceId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ServiceDTO> getServiceById(UUID serviceId) {
        Service service = serviceService.findById(serviceId);
        return ResponseEntity.ok(service.toDTO());
    }

    @Override
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        List<Service> services = serviceService.findAll();
        List<ServiceDTO> serviceDTOs = services.stream().map(Service::toDTO).toList();
        return ResponseEntity.ok(serviceDTOs);
    }

    @Override
    public ResponseEntity<ServiceDTO> updateService(UUID serviceId, @Valid ServiceDTO serviceDTO) {
        serviceDTO.setId(serviceId);
        Service service = Service.fromDTO(serviceDTO);
        Service updatedService = serviceService.update(service);
        return ResponseEntity.ok(updatedService.toDTO());
    }
}

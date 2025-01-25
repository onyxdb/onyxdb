//package com.onyxdb.idm.controllers.v1;
//
//import com.onyxdb.idm.generated.openapi.apis.ServicesApi;
//import com.onyxdb.idm.generated.openapi.models.ServiceDTO;
//import com.onyxdb.idm.generated.openapi.models.BadRequestResponse;
//import com.onyxdb.idm.generated.openapi.models.NotFoundResponse;
//import com.onyxdb.idm.models.Service;
//import com.onyxdb.idm.services.ServiceService;
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
//public class ServicesController implements ServicesApi {
//
//    private final ServiceService serviceService;
//
//    @Override
//    public ResponseEntity<ServiceDTO> createService(@Valid ServiceDTO serviceDTO) {
//        Service service = Service.fromDTO(serviceDTO);
//        serviceService.create(service);
//        return new ResponseEntity<>(service.toDTO(), HttpStatus.CREATED);
//    }
//
//    @Override
//    public ResponseEntity<Void> deleteService(UUID serviceId) {
//        Optional<Service> service = serviceService.findById(serviceId);
//        if (service.isPresent()) {
//            serviceService.delete(serviceId);
//            return ResponseEntity.noContent().build();
//        } else {
//            NotFoundResponse notFoundResponse = new NotFoundResponse();
//            notFoundResponse.setMessage("Service not found");
//            return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @Override
//    public ResponseEntity<ServiceDTO> getServiceById(UUID serviceId) {
//        Optional<Service> service = serviceService.findById(serviceId);
//        if (service.isPresent()) {
//            return ResponseEntity.ok(service.get().toDTO());
//        } else {
//            NotFoundResponse notFoundResponse = new NotFoundResponse();
//            notFoundResponse.setMessage("Service not found");
//            return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @Override
//    public ResponseEntity<List<ServiceDTO>> getAllServices() {
//        List<Service> services = serviceService.findAll();
//        List<ServiceDTO> serviceDTOs = services.stream().map(Service::toDTO).toList();
//        return ResponseEntity.ok(serviceDTOs);
//    }
//
//    @Override
//    public ResponseEntity<ServiceDTO> updateService(UUID serviceId, @Valid ServiceDTO serviceDTO) {
//        Optional<Service> existingService = serviceService.findById(serviceId);
//        if (existingService.isPresent()) {
//            Service service = Service.fromDTO(serviceDTO);
//            serviceService.update(service);
//            return ResponseEntity.ok(service.toDTO());
//        } else {
//            NotFoundResponse notFoundResponse = new NotFoundResponse();
//            notFoundResponse.setMessage("Service not found");
//            return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
//        }
//    }
//}
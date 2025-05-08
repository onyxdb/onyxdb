package com.onyxdb.platform.mdb.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.MdbCommonApi;
import com.onyxdb.platform.generated.openapi.models.ListNamespacesResponseDTO;
import com.onyxdb.platform.generated.openapi.models.ListStorageClassesResponseDTO;

@RestController
public class MdbCommonController implements MdbCommonApi {
    private final List<String> storageClasses;
    private final List<String> namespaces;

    public MdbCommonController(
            @Value("${onyxdb.mdb.storage-classes:}")
            String storageClassesString,
            @Value("${onyxdb.mdb.namespaces:}")
            String namespacesString
    ) {
        this.storageClasses = Arrays.stream(storageClassesString.split(","))
                .filter(s -> !s.isEmpty()).
                collect(Collectors.toList());
        this.namespaces = Arrays.stream(namespacesString.split(","))
                .filter(s -> !s.isEmpty()).
                collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<ListStorageClassesResponseDTO> listStorageClasses() {
        return ResponseEntity.ok(new ListStorageClassesResponseDTO(storageClasses));
    }

    @Override
    public ResponseEntity<ListNamespacesResponseDTO> listNamespaces() {
        return ResponseEntity.ok(new ListNamespacesResponseDTO(namespaces));
    }
}

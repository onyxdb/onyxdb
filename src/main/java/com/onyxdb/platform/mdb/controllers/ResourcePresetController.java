package com.onyxdb.platform.mdb.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.ResourcePresetsApi;
import com.onyxdb.platform.generated.openapi.models.CreateResourcePresetRequestDTO;
import com.onyxdb.platform.generated.openapi.models.ListResourcePresetsResponseDTO;
import com.onyxdb.platform.generated.openapi.models.ResourcePresetResponseDTO;
import com.onyxdb.platform.generated.openapi.models.UpdateResourcePresetRequestDTO;
import com.onyxdb.platform.mdb.resourcePresets.ResourcePreset;
import com.onyxdb.platform.mdb.resourcePresets.ResourcePresetMapper;
import com.onyxdb.platform.mdb.resourcePresets.ResourcePresetService;

/**
 * @author foxleren
 */
@RestController
public class ResourcePresetController implements ResourcePresetsApi {
    private final ResourcePresetService resourcePresetService;
    private final ResourcePresetMapper resourcePresetMapper;

    public ResourcePresetController(
            ResourcePresetService resourcePresetService,
            ResourcePresetMapper resourcePresetMapper
    ) {
        this.resourcePresetService = resourcePresetService;
        this.resourcePresetMapper = resourcePresetMapper;
    }

    @Override
    public ResponseEntity<ListResourcePresetsResponseDTO> listResourcePresets() {
        List<ResourcePreset> resourcePresets = resourcePresetService.list();

        var response = new ListResourcePresetsResponseDTO(
                resourcePresets.stream().map(resourcePresetMapper::toResourcePresetResponseDTO).toList()
        );
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<ResourcePresetResponseDTO> getResourcePreset(String resourcePresetId) {
        ResourcePreset resourcePreset = resourcePresetService.getOrThrow(resourcePresetId);

        var response = resourcePresetMapper.toResourcePresetResponseDTO(resourcePreset);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<Void> createResourcePreset(CreateResourcePresetRequestDTO rq) {
        ResourcePreset resourcePreset = resourcePresetMapper.fromCreateResourcePresetRequestDTO(rq);
        resourcePresetService.create(resourcePreset);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateResourcePreset(String resourcePresetId, UpdateResourcePresetRequestDTO rq) {
        ResourcePreset resourcePreset = resourcePresetMapper.fromUpdateResourcePresetRequestDTO(resourcePresetId, rq);
        resourcePresetService.update(resourcePreset);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteResourcePreset(String resourcePresetName) {
        resourcePresetService.delete(resourcePresetName);
        return ResponseEntity.ok().build();
    }
}

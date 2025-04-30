package com.onyxdb.platform.mdb.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.platform.generated.openapi.apis.ResourcePresetsApi;
import com.onyxdb.platform.generated.openapi.models.V1CreateResourcePresetRequest;
import com.onyxdb.platform.generated.openapi.models.V1ListResourcePresetsResponse;
import com.onyxdb.platform.generated.openapi.models.V1ResourcePresetResponse;
import com.onyxdb.platform.generated.openapi.models.V1UpdateResourcePresetRequest;
import com.onyxdb.platform.mdb.resourcePresets.ResourcePreset;
import com.onyxdb.platform.mdb.resourcePresets.ResourcePresetConverter;
import com.onyxdb.platform.mdb.resourcePresets.ResourcePresetService;

/**
 * @author foxleren
 */
@RestController
public class v1ResourcePresetController implements ResourcePresetsApi {
    private final ResourcePresetService resourcePresetService;

    public v1ResourcePresetController(ResourcePresetService resourcePresetService) {
        this.resourcePresetService = resourcePresetService;
    }

    @Override
    public ResponseEntity<V1ListResourcePresetsResponse> listResourcePresets() {
        List<ResourcePreset> resourcePresets = resourcePresetService.list();
        var response = ResourcePresetConverter.toV1ListResourcePresetsResponse(resourcePresets);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<V1ResourcePresetResponse> getResourcePreset(UUID resourcePresetId) {
        ResourcePreset resourcePreset = resourcePresetService.getOrThrow(resourcePresetId);
        var response = ResourcePresetConverter.toV1ResourcePresetResponse(resourcePreset);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<Void> createResourcePreset(V1CreateResourcePresetRequest r) {
        ResourcePreset resourcePreset = ResourcePresetConverter.fromV1CreateResourcePresetRequest(r);
        resourcePresetService.create(resourcePreset);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateResourcePreset(
            UUID resourcePresetId,
            V1UpdateResourcePresetRequest r
    ) {
        ResourcePreset resourcePreset = ResourcePresetConverter.fromV1UpdateResourcePresetRequest(
                resourcePresetId,
                r
        );
        resourcePresetService.update(resourcePreset);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteResourcePreset(UUID resourcePresetId) {
        resourcePresetService.delete(resourcePresetId);
        return ResponseEntity.ok().build();
    }
}

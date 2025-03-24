package com.onyxdb.mdb.controllers.v1;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.onyxdb.mdb.generated.openapi.apis.V1HardwarePresetsApi;
import com.onyxdb.mdb.generated.openapi.models.V1CreateHardwarePresetRequest;
import com.onyxdb.mdb.generated.openapi.models.V1HardwarePresetResponse;
import com.onyxdb.mdb.generated.openapi.models.V1ListHardwarePresetsResponse;
import com.onyxdb.mdb.generated.openapi.models.V1UpdateHardwarePresetRequest;
import com.onyxdb.mdb.hardwarePresets.HardwarePreset;
import com.onyxdb.mdb.hardwarePresets.HardwarePresetConverter;
import com.onyxdb.mdb.hardwarePresets.HardwarePresetService;

/**
 * @author foxleren
 */
@RestController
public class V1HardwarePresetController implements V1HardwarePresetsApi {
    private final HardwarePresetService hardwarePresetService;

    public V1HardwarePresetController(HardwarePresetService hardwarePresetService) {
        this.hardwarePresetService = hardwarePresetService;
    }

    @Override
    public ResponseEntity<Void> createHardwarePreset(V1CreateHardwarePresetRequest v1CreateHardwarePresetRequest) {
        HardwarePreset hardwarePreset = HardwarePresetConverter
                .fromV1CreateHardwarePresetRequest(v1CreateHardwarePresetRequest);
        hardwarePresetService.create(hardwarePreset);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteHardwarePreset(String hardwarePresetId) {
        hardwarePresetService.delete(hardwarePresetId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<V1HardwarePresetResponse> getHardwarePreset(String hardwarePresetId) {
        HardwarePreset hardwarePreset = hardwarePresetService.get(hardwarePresetId);
        var response = HardwarePresetConverter.toV1HardwarePresetResponse(hardwarePreset);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<V1ListHardwarePresetsResponse> listHardwarePresets() {
        List<HardwarePreset> hardwarePresets = hardwarePresetService.list();
        var response = HardwarePresetConverter.toV1ListHardwarePresetsResponse(hardwarePresets);
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<Void> updateHardwarePreset(
            String hardwarePresetId,
            V1UpdateHardwarePresetRequest v1UpdateHardwarePresetRequest
    ) {
        HardwarePreset hardwarePreset = HardwarePresetConverter.fromV1UpdateHardwarePresetRequest(
                hardwarePresetId,
                v1UpdateHardwarePresetRequest
        );
        hardwarePresetService.update(hardwarePreset);
        return ResponseEntity.ok().build();
    }
}

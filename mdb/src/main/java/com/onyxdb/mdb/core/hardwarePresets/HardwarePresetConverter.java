package com.onyxdb.mdb.core.hardwarePresets;

import java.util.List;

import org.jooq.Record;

import com.onyxdb.mdb.generated.jooq.tables.records.HardwarePresetsRecord;
import com.onyxdb.mdb.generated.openapi.models.V1CreateHardwarePresetRequest;
import com.onyxdb.mdb.generated.openapi.models.V1HardwarePresetResponse;
import com.onyxdb.mdb.generated.openapi.models.V1HardwarePresetType;
import com.onyxdb.mdb.generated.openapi.models.V1ListHardwarePresetsResponse;
import com.onyxdb.mdb.generated.openapi.models.V1UpdateHardwarePresetRequest;

/**
 * @author foxleren
 */
public final class HardwarePresetConverter {
    public static HardwarePreset fromV1CreateHardwarePresetRequest(V1CreateHardwarePresetRequest r) {
        return new HardwarePreset(
                r.getId(),
                HardwarePresetType.R.fromValue(r.getType().getValue()),
                r.getVcpu(),
                r.getRam()
        );
    }

    public static V1HardwarePresetResponse toV1HardwarePresetResponse(HardwarePreset p) {
        return new V1HardwarePresetResponse(
                p.id(),
                V1HardwarePresetType.fromValue(p.type().value()),
                p.vcpu(),
                p.ram()
        );
    }

    public static V1ListHardwarePresetsResponse toV1ListHardwarePresetsResponse(List<HardwarePreset> p) {
        return new V1ListHardwarePresetsResponse(
                p.stream()
                        .map(HardwarePresetConverter::toV1HardwarePresetResponse)
                        .toList()
        );
    }

    public static HardwarePreset fromV1UpdateHardwarePresetRequest(
            String hardwarePresetId,
            V1UpdateHardwarePresetRequest r
    ) {
        return new HardwarePreset(
                hardwarePresetId,
                HardwarePresetType.R.fromValue(r.getType().getValue()),
                r.getVcpu(),
                r.getRam()
        );
    }

    public static HardwarePreset fromJooqRecord(Record r) {
        HardwarePresetsRecord rr = r.into(HardwarePresetsRecord.class);
        return new HardwarePreset(
                rr.getId(),
                HardwarePresetType.R.fromValue(rr.getType().getLiteral()),
                rr.getVcpu(),
                rr.getRam()
        );
    }

    public static HardwarePresetsRecord toHardwarePresetsRecord(HardwarePreset p) {
        return new HardwarePresetsRecord(
                p.id(),
                p.type().toJooq(),
                p.vcpu(),
                p.ram()
        );
    }
}

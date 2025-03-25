package com.onyxdb.mdb.core.resourcePresets;

import java.util.List;

import org.jooq.Record;

import com.onyxdb.mdb.generated.jooq.tables.records.ResourcePresetsRecord;
import com.onyxdb.mdb.generated.openapi.models.V1CreateResourcePresetRequest;
import com.onyxdb.mdb.generated.openapi.models.V1ListResourcePresetsResponse;
import com.onyxdb.mdb.generated.openapi.models.V1ResourcePresetResponse;
import com.onyxdb.mdb.generated.openapi.models.V1ResourcePresetType;
import com.onyxdb.mdb.generated.openapi.models.V1UpdateResourcePresetRequest;

/**
 * @author foxleren
 */
public final class ResourcePresetConverter {
    public static ResourcePreset fromV1CreateResourcePresetRequest(V1CreateResourcePresetRequest r) {
        return new ResourcePreset(
                r.getId(),
                ResourcePresetType.R.fromValue(r.getType().getValue()),
                r.getVcpu(),
                r.getRam()
        );
    }

    public static V1ResourcePresetResponse toV1ResourcePresetResponse(ResourcePreset p) {
        return new V1ResourcePresetResponse(
                p.id(),
                V1ResourcePresetType.fromValue(p.type().value()),
                p.vcpu(),
                p.ram()
        );
    }

    public static V1ListResourcePresetsResponse toV1ListResourcePresetsResponse(List<ResourcePreset> p) {
        return new V1ListResourcePresetsResponse(
                p.stream()
                        .map(ResourcePresetConverter::toV1ResourcePresetResponse)
                        .toList()
        );
    }

    public static ResourcePreset fromV1UpdateResourcePresetRequest(
            String id,
            V1UpdateResourcePresetRequest r
    ) {
        return new ResourcePreset(
                id,
                ResourcePresetType.R.fromValue(r.getType().getValue()),
                r.getVcpu(),
                r.getRam()
        );
    }

    public static ResourcePreset fromJooqRecord(Record r) {
        ResourcePresetsRecord rr = r.into(ResourcePresetsRecord.class);
        return new ResourcePreset(
                rr.getId(),
                ResourcePresetType.R.fromValue(rr.getType().getLiteral()),
                rr.getVcpu(),
                rr.getRam()
        );
    }

    public static ResourcePresetsRecord toResourcePresetsRecord(ResourcePreset p) {
        return new ResourcePresetsRecord(
                p.id(),
                p.type().toJooq(),
                p.vcpu(),
                p.ram()
        );
    }
}

package com.onyxdb.platform.mdb.resourcePresets;

import org.jooq.Record;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.generated.jooq.tables.records.ResourcePresetsRecord;
import com.onyxdb.platform.generated.openapi.models.CreateResourcePresetRequestDTO;
import com.onyxdb.platform.generated.openapi.models.ResourcePresetResponseDTO;
import com.onyxdb.platform.generated.openapi.models.UpdateResourcePresetRequestDTO;

/**
 * @author foxleren
 */
@Component
public final class ResourcePresetMapper {
    public ResourcePreset fromCreateResourcePresetRequestDTO(CreateResourcePresetRequestDTO rq) {
        return new ResourcePreset(
                rq.getId(),
                ResourcePresetType.R.fromValue(rq.getType()),
                rq.getVcpu(),
                rq.getRam()
        );
    }

    public ResourcePresetResponseDTO toResourcePresetResponseDTO(ResourcePreset p) {
        return new ResourcePresetResponseDTO(
                p.id(),
                p.type().value(),
                p.vcpu(),
                p.ram()
        );
    }

    public ResourcePreset fromUpdateResourcePresetRequestDTO(String resourcePresetId, UpdateResourcePresetRequestDTO rq) {
        return new ResourcePreset(
                resourcePresetId,
                ResourcePresetType.R.fromValue(rq.getType()),
                rq.getVcpu(),
                rq.getRam()
        );
    }

    public ResourcePreset fromJooqRecord(Record r) {
        ResourcePresetsRecord rr = r.into(ResourcePresetsRecord.class);
        return new ResourcePreset(
                rr.getId(),
                ResourcePresetType.R.fromValue(rr.getType().getLiteral()),
                rr.getVcpu(),
                rr.getRam()
        );
    }

    public ResourcePresetsRecord toResourcePresetsRecord(ResourcePreset p) {
        return new ResourcePresetsRecord(
                p.id(),
                p.type().toJooq(),
                p.vcpu(),
                p.ram()
        );
    }
}

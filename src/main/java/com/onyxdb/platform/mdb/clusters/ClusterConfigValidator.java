package com.onyxdb.platform.mdb.clusters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.onyxdb.platform.mdb.clusters.models.ClusterConfig;
import com.onyxdb.platform.mdb.resourcePresets.ResourcePresetService;

@Component
@RequiredArgsConstructor
public class ClusterConfigValidator {
    private final ResourcePresetService resourcePresetService;

    public void validate(ClusterConfig clusterConfig) {
        resourcePresetService.getOrThrow(clusterConfig.resources().presetId());
    }
}

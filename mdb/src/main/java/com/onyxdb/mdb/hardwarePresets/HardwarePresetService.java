package com.onyxdb.mdb.hardwarePresets;

import java.util.List;
import java.util.Optional;

import com.onyxdb.mdb.exceptions.BadRequestException;

/**
 * @author foxleren
 */
public class HardwarePresetService {
    private final HardwarePresetRepository hardwarePresetRepository;

    public HardwarePresetService(HardwarePresetRepository hardwarePresetRepository) {
        this.hardwarePresetRepository = hardwarePresetRepository;
    }

    public List<HardwarePreset> list() {
        return hardwarePresetRepository.list();
    }

    public Optional<HardwarePreset> getO(String id) {
        return hardwarePresetRepository.getO(id);
    }

    public HardwarePreset get(String id) {
        return getO(id).orElseThrow(() -> new BadRequestException("Can't get hardware preset with id=" + id));
    }

    public void create(HardwarePreset hardwarePreset) {
        hardwarePresetRepository.create(hardwarePreset);
    }

    public void update(HardwarePreset hardwarePreset) {
        hardwarePresetRepository.update(hardwarePreset);
    }

    public void delete(String id) {
        hardwarePresetRepository.delete(id);
    }
}

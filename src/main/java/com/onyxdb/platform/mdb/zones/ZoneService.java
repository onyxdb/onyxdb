package com.onyxdb.platform.mdb.zones;

import java.util.List;
import java.util.Optional;

import com.onyxdb.platform.mdb.exceptions.BadRequestException;

/**
 * @author foxleren
 */
public class ZoneService {
    private final ZoneRepository zoneRepository;

    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public List<Zone> list() {
        return zoneRepository.list();
    }

    public Optional<Zone> getO(String id) {
        return zoneRepository.getO(id);
    }

    public Zone getOrThrow(String id) {
        return getO(id).orElseThrow(() -> new BadRequestException("Can't get zone with id=" + id));
    }

    public void create(Zone zone) {
        zoneRepository.create(zone);
    }

    public void update(Zone zone) {
        zoneRepository.update(zone);
    }

    public void delete(String id) {
        zoneRepository.delete(id);
    }
}

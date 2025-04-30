package com.onyxdb.platform.mdb.zones;

import java.util.List;
import java.util.Optional;

/**
 * @author foxleren
 */
public interface ZoneRepository {
    List<Zone> list();

    Optional<Zone> getO(String id);

    void create(Zone zone);

    void update(Zone zone);

    void delete(String id);
}

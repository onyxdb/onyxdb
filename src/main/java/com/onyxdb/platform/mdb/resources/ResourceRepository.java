package com.onyxdb.platform.mdb.resources;

import java.util.List;

public interface ResourceRepository {
    List<Resource> listResources(ResourceFilter filter);
}

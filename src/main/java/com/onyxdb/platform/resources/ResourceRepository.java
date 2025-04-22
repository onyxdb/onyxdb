package com.onyxdb.platform.resources;

import java.util.List;

public interface ResourceRepository {
    List<Resource> listResources(ResourceFilter filter);
}

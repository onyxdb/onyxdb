package com.onyxdb.platform.core.clusters.models;

import com.onyxdb.platform.core.projects.Project;

public record ProjectAndCluster(
        Project project,
        Cluster cluster
) {
}

package com.onyxdb.platform.core.clusters.models;

import com.onyxdb.platform.projects.Project;

public record ProjectAndCluster(
        Project project,
        Cluster cluster
) {
}

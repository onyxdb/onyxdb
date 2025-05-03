package com.onyxdb.platform.mdb.clusters.models;

import com.onyxdb.platform.mdb.projects.Project;

public record ProjectAndCluster(
        Project project,
        Cluster cluster
) {
}

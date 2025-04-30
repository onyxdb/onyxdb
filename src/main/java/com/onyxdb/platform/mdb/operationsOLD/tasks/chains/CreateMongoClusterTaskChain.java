package com.onyxdb.platform.mdb.operationsOLD.tasks.chains;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;

public class CreateMongoClusterTaskChain extends TaskChain {
    @Nullable
    public UUID createMongoVectorConfigTaskId;
    @Nullable
    public UUID createPsmdbTaskId;
    @Nullable
    public UUID checkPsmdbReadinessTaskId;
    @Nullable
    public UUID createOnyxdbAgentTaskId;
    @Nullable
    public UUID checkOnyxdbAgentReadinessTaskId;
    @Nullable
    public UUID createMongoExporterServiceTaskId;
    @Nullable
    public UUID createMongoExporterServiceScrapeTaskId;
    @Nullable
    public UUID createMongoDatabaseTaskId;
    @Nullable
    public UUID createMongoUserTaskId;
}

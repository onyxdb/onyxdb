package com.onyxdb.platform.mdb.initialization;

public interface InitializationRepository {
    boolean getIsInitializedForUpdate();

    void markAsInitialized();
}

package com.onyxdb.mongodbOperator.crds;

/**
 * @author foxleren
 */
// TODO добавить поддержку опциональных параметров
public class ManagedMongodbSpec {
    private long diskSizeBytes = 1;

    public long getDiskSizeBytes() {
        return diskSizeBytes;
    }

    public void setDiskSizeBytes(long diskSizeBytes) {
        this.diskSizeBytes = diskSizeBytes;
    }
}

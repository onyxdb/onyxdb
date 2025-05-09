package com.onyxdb.platform.mdb.backups;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BackupService {
    private final String minioUrl;
    private final String minioSecret;
    private final String minioBucket;

    public BackupService(
            @Value("${onyxdb.minio.url}")
            String minioUrl,
            @Value("${onyxdb.minio.secret}")
            String minioSecret,
            @Value("${onyxdb.minio.bucket}")
            String minioBucket
    ) {
        this.minioUrl = minioUrl;
        this.minioSecret = minioSecret;
        this.minioBucket = minioBucket;
    }

    public String getMinioUrl() {
        return minioUrl;
    }

    public String getMinioSecret() {
        return minioSecret;
    }

    public String getMinioBucket() {
        return minioBucket;
    }
}

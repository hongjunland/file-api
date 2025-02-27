package com.example.fileapi.config;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class GcpConfig {
    private final GcpProperties gcpProperties;

    @Bean
    public Storage googleCloudStorage() throws IOException {
        return StorageOptions.newBuilder()
                .setProjectId(gcpProperties.getProjectId())
                .setCredentials(ServiceAccountCredentials.fromStream(
                        new FileInputStream(gcpProperties.getCredentials().getLocation()) // 👈 직접 서비스 계정 키 로드
                ))
                .build()
                .getService();
    }
}


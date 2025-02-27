package com.example.fileapi.service;

import com.example.fileapi.config.GcpProperties;
import com.example.fileapi.dto.SignedUrlRequest;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SignedUrlService {

    private final Storage storage;
    private final GcpProperties gcpProperties;
    public String generateSignedUrl(SignedUrlRequest signedUrlRequest) {
        try {
            BlobInfo blobInfo = BlobInfo.newBuilder(gcpProperties.getBucket(), signedUrlRequest.objectName()).build();

            URL signedUrl = storage.signUrl(
                    blobInfo,
                    15,
                    TimeUnit.MINUTES,
                    Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
                    Storage.SignUrlOption.withV4Signature() // V4 서명 방식 사용,
            );
            return signedUrl.toString();
        } catch (Exception e) {
            throw new RuntimeException("서명된 URL 생성 실패", e);
        }
    }

}

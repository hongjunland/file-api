package com.example.fileapi.service;

import com.example.fileapi.config.GcpProperties;
import com.example.fileapi.dto.SignedUrlRequest;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SignedUrlService {

    private final Storage storage;
    private final GcpProperties gcpProperties;

    /**
     * 📌 업로드용 Signed URL 생성 (PUT 요청)
     * @param signedUrlRequest 파일 경로
     * @return Signed URL (5분간 유효)
     */
    public String generateUploadSignedUrl(SignedUrlRequest signedUrlRequest) {
        try {
            BlobInfo blobInfo = BlobInfo.newBuilder(gcpProperties.getBucket(), signedUrlRequest.filePath()).build();
            URL signedUrl = storage.signUrl(
                    blobInfo,
                    5,
                    TimeUnit.MINUTES,
                    Storage.SignUrlOption.httpMethod(HttpMethod.PUT), // upload는 PUT or POST
                    Storage.SignUrlOption.withV4Signature() // V4 서명 방식 사용,
            );
            return signedUrl.toString();
        } catch (Exception e) {
            throw new RuntimeException("서명된 URL 생성 실패", e);
        }
    }

    /**
     * 📌 다운로드용 Signed URL 생성 (GET 요청)
     * @param signedUrlRequest 파일 경로
     * @return Signed URL (5분간 유효, 브라우저에서 다운로드 가능)
     */
    public String generateDownloadSignedUrl(SignedUrlRequest signedUrlRequest) {
        try {
            BlobInfo blobInfo = BlobInfo.newBuilder(gcpProperties.getBucket(), signedUrlRequest.filePath()).build();
            URL signedUrl = storage.signUrl(
                    blobInfo,
                    5,
                    TimeUnit.MINUTES,
                    Storage.SignUrlOption.httpMethod(HttpMethod.GET), // download는 GET
                    Storage.SignUrlOption.withV4Signature(), // V4 서명 방식 사용,
                    Storage.SignUrlOption.withQueryParams(Map.of("response-content-disposition", "attachment")) // 브라우저 접속시 다운로드
            );
            return signedUrl.toString();
        } catch (Exception e) {
            throw new RuntimeException("서명된 URL 생성 실패", e);
        }
    }

}

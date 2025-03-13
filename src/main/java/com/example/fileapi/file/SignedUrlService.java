package com.example.fileapi.file;

import com.example.fileapi.file.config.GcpProperties;
import com.example.fileapi.file.dto.FileResponse;
import com.example.fileapi.file.dto.FileUploadStatusUpdateRequest;
import com.example.fileapi.file.dto.SignedUrlRequest;
import com.example.fileapi.file.enums.FileUploadStatus;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SignedUrlService {
    private final FileJpaRepository fileJpaRepository;
    private final Storage storage;
    private final GcpProperties gcpProperties;


    /**
     * 📌 업로드용 Signed URL 생성 (PUT 요청)
     *
     * @param signedUrlRequest 파일 경로
     * @return Signed URL (5분간 유효)
     */
    @Transactional
    public String generateUploadSignedUrl(SignedUrlRequest signedUrlRequest) {
        try {
            String storedFilename = generateFileName(signedUrlRequest.filename());
            BlobInfo blobInfo = BlobInfo.newBuilder(gcpProperties.getBucket(), storedFilename).build();
            URL signedUrl = storage.signUrl(
                    blobInfo,
                    5,
                    TimeUnit.MINUTES,
                    Storage.SignUrlOption.httpMethod(HttpMethod.PUT), // upload는 PUT or POST
                    Storage.SignUrlOption.withV4Signature() // V4 서명 방식 사용,
            );
            fileJpaRepository.save(FileJpaEntity.builder()
                    .originalFilename(signedUrlRequest.filename())
                    .storedFilename(storedFilename)
                    .fileUrl(String.format("https://storage.googleapis.com/%s/%s", gcpProperties.getBucket(), storedFilename))
                    .fileType(signedUrlRequest.fileType())
                    .fileUploadStatus(FileUploadStatus.PENDING)
                    .build());
            return signedUrl.toString();
        } catch (Exception e) {
            throw new RuntimeException("서명된 URL 생성 실패", e);
        }
    }

    /**
     * 📌 다운로드용 Signed URL 생성 (GET 요청)
     *
     * @param signedUrlRequest 파일 경로
     * @return Signed URL (5분간 유효, 브라우저에서 다운로드 가능)
     */
    public String generateDownloadSignedUrl(SignedUrlRequest signedUrlRequest) {
        try {
            BlobInfo blobInfo = BlobInfo.newBuilder(gcpProperties.getBucket(), signedUrlRequest.filename()).build();
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

    private String generateFileName(String originalFilename) {
        return (UUID.randomUUID() + "_" + originalFilename);
    }

    @Transactional
    public void updateFileUploadStatus(FileUploadStatusUpdateRequest fileUploadStatusUpdateRequest) {
        FileJpaEntity fileJpaEntity = fileJpaRepository.findById(fileUploadStatusUpdateRequest.fileId())
                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다."));
        fileJpaEntity.updateFileUploadStatus(fileUploadStatusUpdateRequest.status());
        fileJpaRepository.save(fileJpaEntity);
    }

    @Transactional(readOnly = true)
    public FileResponse findFileById(Long fileId) {
        FileJpaEntity fileJpaEntity = fileJpaRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다."));
        return FileResponse.builder()
                .fileId(fileJpaEntity.getFileId())
                .fileName(fileJpaEntity.getOriginalFilename())
                .fileUrl(fileJpaEntity.getFileUrl())
                .fileType(fileJpaEntity.getFileType())
                .status(fileJpaEntity.getFileUploadStatus())
                .build();
    }
}

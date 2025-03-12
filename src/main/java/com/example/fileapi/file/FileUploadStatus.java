package com.example.fileapi.file;

public enum FileUploadStatus {
    PENDING,   // (1) 업로드 대기 (Signed URL 발급 후)
    SUCCESS,   // (2) 업로드 성공 (클라이언트가 서버에 성공 응답 보냄)
    FAILED     // (3) 업로드 실패 (클라이언트가 실패 응답 보냄)
}

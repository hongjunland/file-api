package com.example.fileapi.file.dto;

import com.example.fileapi.file.FileUploadStatus;

public record FileUploadStatusUpdateRequest(Long fileId, FileUploadStatus status) {
}

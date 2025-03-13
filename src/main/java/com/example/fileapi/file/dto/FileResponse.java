package com.example.fileapi.file.dto;

import com.example.fileapi.file.enums.FileUploadStatus;
import lombok.Builder;

@Builder
public record FileResponse(
        Long fileId,
        String fileName,
        String fileUrl,
        String fileType,
        FileUploadStatus status
) {
}


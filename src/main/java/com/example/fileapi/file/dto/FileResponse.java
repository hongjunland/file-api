package com.example.fileapi.file.dto;

import lombok.Builder;

@Builder
public record FileResponse(
        Long fileId,
        String fileName,
        String fileUrl
) {
}


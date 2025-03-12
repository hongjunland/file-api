package com.example.fileapi.file.dto;

import jakarta.validation.constraints.NotBlank;

public record SignedUrlRequest(@NotBlank String filename, @NotBlank String fileType) {
}

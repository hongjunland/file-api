package com.example.fileapi.controller;

import com.example.fileapi.dto.SignedUrlRequest;
import com.example.fileapi.service.SignedUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final SignedUrlService signedUrlService;

    @PostMapping("/signed-url/upload")
    public ResponseEntity<?> generateUpLoadSignedUrl(@RequestBody SignedUrlRequest signedUrlRequest) {
        return ResponseEntity.ok(signedUrlService.generateUploadSignedUrl( signedUrlRequest));
    }
    @PostMapping("/signed-url/download")
    public ResponseEntity<?> generateDownLoadSignedUrl(@RequestBody SignedUrlRequest signedUrlRequest) {
        return ResponseEntity.ok(signedUrlService.generateDownloadSignedUrl( signedUrlRequest));
    }

}

package com.example.fileapi.file;

import com.example.fileapi.file.dto.SignedUrlRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final SignedUrlService signedUrlService;

    @PostMapping("/signed-url/upload")
    public ResponseEntity<?> generateUpLoadSignedUrl(@Valid @RequestBody SignedUrlRequest signedUrlRequest) {
        return ResponseEntity.ok(signedUrlService.generateUploadSignedUrl(signedUrlRequest));
    }
    @PostMapping("/signed-url/download")
    public ResponseEntity<?> generateDownLoadSignedUrl(@RequestBody SignedUrlRequest signedUrlRequest) {
        return ResponseEntity.ok(signedUrlService.generateDownloadSignedUrl( signedUrlRequest));
    }

}

package com.example.fileapi.file;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "files")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;
    private String originalFilename;  // 업로드된 원본 파일명
    private String storedFilename;    // GCS에 저장된 파일명 (UUID 포함)
    private String fileUrl;           // GCS에 저장된 전체 URL
}

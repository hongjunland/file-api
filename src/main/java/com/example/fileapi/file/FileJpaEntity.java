package com.example.fileapi.file;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    private String fileType;         // MIME 타입 (image/png, video/mp4 등)
    private String originalFilename; // 원본 파일명
    private String storedFilename;   // UUID 기반 파일명
    private String fileUrl;          // GCS URL

    @Enumerated(EnumType.ORDINAL)
    private FileUploadStatus fileUploadStatus; // 업로드 상태 (PENDING, SUCCESS, FAILED)

    public void updateFileUploadStatus(FileUploadStatus status){
        this.fileUploadStatus = status;
    }
}

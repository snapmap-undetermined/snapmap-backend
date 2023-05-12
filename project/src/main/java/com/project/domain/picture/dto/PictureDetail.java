package com.project.domain.picture.dto;

import com.project.common.utils.FileUtils;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PictureDetail {
    private String id;
    private String name;
    private String format;
    private String path;
    private long bytes;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public static PictureDetail multipartOf(MultipartFile multipartFile) {
        final String fileId = FileUtils.createFileId();
        final String format = FileUtils.getFormat(multipartFile.getContentType());
        return PictureDetail.builder()
                .id(fileId)
                .name(multipartFile.getOriginalFilename())
                .format(format)
                .path(FileUtils.createPath(fileId, format))
                .bytes(multipartFile.getSize())
                .build();
    }
}

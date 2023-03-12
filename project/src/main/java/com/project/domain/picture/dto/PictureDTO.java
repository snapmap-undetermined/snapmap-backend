package com.project.domain.picture.dto;

import com.project.domain.picture.entity.Picture;
import lombok.Data;

import java.time.LocalDateTime;

public class PictureDTO {

    @Data
    public static class PictureResponse {
        private Long id;
        private String uri;
        private String originalName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        public PictureResponse(Picture picture) {
            this.id = picture.getId();
            this.uri = picture.getUrl();
            this.originalName = picture.getOriginalName();
            this.createdAt = picture.getCreatedAt();
            this.updatedAt = picture.getModifiedAt();
        }
    }

}



package com.project.domain.picture.dto;

import com.project.domain.comment.dto.PictureCommentDTO;
import com.project.domain.picture.entity.Picture;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class PictureDTO {

    @Data
    public static class PictureResponse {
        private Long id;
        private String uri;
        private String originalName;
        private PictureCommentDTO.PictureCommentListResponse pictureCommentList;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public PictureResponse(Picture picture) {
            this.id = picture.getId();
            this.uri = picture.getUrl();
            this.originalName = picture.getOriginalName();
            this.pictureCommentList = new PictureCommentDTO.PictureCommentListResponse(picture.getCommentList().stream()
                    .map(PictureCommentDTO.PictureCommentDetailResponse::new).collect(Collectors.toList()));
            this.createdAt = picture.getCreatedAt();
            this.updatedAt = picture.getModifiedAt();
        }
    }

}



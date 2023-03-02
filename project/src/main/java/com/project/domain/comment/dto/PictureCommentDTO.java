package com.project.domain.comment.dto;

import com.project.domain.comment.entity.PictureComment;
import com.project.domain.picture.entity.Picture;
import com.project.domain.users.entity.Users;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

public class PictureCommentDTO {

    @Data
    public static class PictureCommentDetailResponse {
        private Long commentId;
        private Long writerId;
        private String text;
        private Long parentCommentOrder;
        private Long commentOrder;
        private Boolean isDeleted;
        private int childCommentCount;

        public PictureCommentDetailResponse(PictureComment pictureComment) {
            this.commentId = pictureComment.getId();
            this.writerId = pictureComment.getWriter().getId();
            this.text = pictureComment.getText();
            this.parentCommentOrder = pictureComment.getParentCommentOrder();
            this.commentOrder = pictureComment.getCommentOrder();
            this.isDeleted = pictureComment.getIsDeleted();
            this.childCommentCount = pictureComment.getChildCommentCount();
        }
    }

    @Data
    public static class CreatePictureCommentRequest {

        @NotEmpty
        private Long pictureId;
        @NotEmpty
        private String text;

        private Long parentCommentOrder;

        public PictureComment toEntity(Users user, Picture picture) {
            return PictureComment.builder()
                    .picture(picture)
                    .text(text)
                    .parentCommentOrder(parentCommentOrder)
                    .childCommentCount(0)
                    .isDeleted(false)
                    .writer(user)
                    .build();
        }
    }

    @Data
    public static class PictureCommentListResponse {
        private List<PictureCommentDTO.PictureCommentDetailResponse> pictureCommentListResponse;

        public PictureCommentListResponse(List<PictureCommentDTO.PictureCommentDetailResponse> pictureCommentListResponse) {
            this.pictureCommentListResponse = pictureCommentListResponse;
        }
    }

    @Data
    public static class UpdatePictureCommentRequest {
        private String text;
    }

}

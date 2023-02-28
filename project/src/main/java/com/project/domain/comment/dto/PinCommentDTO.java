package com.project.domain.comment.dto;

import com.project.domain.comment.entity.PinComment;
import com.project.domain.pin.entity.Pin;
import com.project.domain.users.entity.Users;
import lombok.Data;

import java.util.List;

public class PinCommentDTO {

    @Data
    public static class PinCommentDetailResponse {
        private Long commentId;
        private Long writerId;
        private String text;
        private Long parentCommentId;
        private Long commentOrder;
        private Boolean isDeleted;
        private Long childCommentCount;

        public PinCommentDetailResponse(PinComment pinComment) {
            this.commentId = pinComment.getId();
            this.writerId = pinComment.getWriter().getId();
            this.text = pinComment.getText();
            this.parentCommentId = pinComment.getParentCommentId();
            this.commentOrder = pinComment.getCommentOrder();
            this.isDeleted = pinComment.getIsDeleted();
            this.childCommentCount = pinComment.getChildCommentCount();
        }
    }

    @Data
    public static class CreatePinCommentRequest {
        private Long pinId;
        private String text;
        private Long parentCommentId;
        private Long commentOrder;
        private Boolean isDeleted;

        public PinComment toEntity(Users user, Pin pin) {
            return PinComment.builder()
                    .pin(pin)
                    .parentCommentId(parentCommentId)
                    .text(text)
                    .parentCommentId(parentCommentId)
                    .commentOrder(commentOrder)
                    .isDeleted(false)
                    .hateCount(0)
                    .likeCount(0)
                    .writer(user)
                    .build();
        }
    }

    @Data
    public static class PinCommentListResponse {
        private List<PinCommentDetailResponse> pinCommentListResponse;

        public PinCommentListResponse(List<PinCommentDetailResponse> pinCommentListResponse) {
            this.pinCommentListResponse = pinCommentListResponse;
        }
    }

    @Data
    public static class UpdatePinCommentRequest {
        private String text;
    }
}

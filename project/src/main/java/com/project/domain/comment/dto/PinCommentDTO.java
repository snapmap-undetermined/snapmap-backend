package com.project.domain.comment.dto;

import com.project.domain.comment.entity.PinComment;
import com.project.domain.pin.entity.Pin;
import com.project.domain.users.entity.Users;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

public class PinCommentDTO {

    @Data
    public static class PinCommentDetailResponse {
        private Long commentId;
        private Long writerId;
        private String writerNickname;
        private String text;
        private Long parentCommentOrder;
        private Long commentOrder;
        private Boolean isDeleted;
        private int childCommentCount;

        public PinCommentDetailResponse(PinComment pinComment) {
            this.commentId = pinComment.getId();
            this.writerId = pinComment.getWriter().getId();
            this.writerNickname = pinComment.getWriter().getNickname();
            this.text = pinComment.getText();
            this.parentCommentOrder = pinComment.getParentCommentOrder();
            this.commentOrder = pinComment.getCommentOrder();
            this.isDeleted = pinComment.getIsDeleted();
            this.childCommentCount = pinComment.getChildCommentCount();
        }
    }

    @Data
    public static class CreatePinCommentRequest {
        @NotBlank(message = "최소 한 글자 이상을 입력해야 합니다.")
        private String text;

        private Long parentCommentOrder;

        public PinComment toEntity(Users user, Pin pin) {
            return PinComment.builder()
                    .pin(pin)
                    .text(text)
                    .parentCommentOrder(parentCommentOrder)
                    .childCommentCount(0)
                    .isDeleted(false)
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
        @NotBlank(message = "최소 한 글자 이상을 입력해야 합니다.")
        private String text;
    }
}

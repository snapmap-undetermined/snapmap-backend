package com.project.album.domain.usercircle.dto;

import com.project.album.domain.usercircle.entity.UserCircle;
import lombok.Data;

import java.time.LocalDateTime;

public class UserCircleDTO {

    @Data
    public static class UserCircleSimpleInfoResponse {
        private Long userId;
        private String userNickname;
        private String userProfileImage;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public UserCircleSimpleInfoResponse(UserCircle userCircle) {
            this.userId = userCircle.getUser().getId();
            this.userNickname = userCircle.getUser().getNickname();
            this.userProfileImage = userCircle.getUser().getProfileImage();
            this.createdAt = userCircle.getCreatedAt();
            this.updatedAt = userCircle.getModifiedAt();
        }


    }

}

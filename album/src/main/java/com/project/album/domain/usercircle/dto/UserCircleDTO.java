package com.project.album.domain.usercircle.dto;

import com.project.album.domain.usercircle.entity.UserCircle;
import lombok.Data;

public class UserCircleDTO {

    @Data
    public static class JoinUserCircleFromLinkRequest {

        private String circleKey;

        public UserCircle toEntity() {
            return UserCircle.builder()
                    .build();
        }
    }

    @Data
    public static class JoinUserCircleInAppRequest {

        private Long circleId;

        public JoinUserCircleInAppRequest(UserCircle userCircle) {
            this.circleId = userCircle.getCircle().getId();
        }

        public UserCircle toEntity() {
            return UserCircle.builder()
                    .build();
        }
    }

    @Data
    public static class UserCircleSimpleInfoResponse {
        private Long userId;
        private String userNickname;
        private String userProfileImage;

        public UserCircleSimpleInfoResponse(UserCircle userCircle) {
            this.userId = userCircle.getUser().getId();
            this.userNickname = userCircle.getUser().getNickname();
            this.userProfileImage = userCircle.getUser().getProfileImage();
        }


    }

}

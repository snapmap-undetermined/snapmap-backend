package com.project.domain.circle.dto;


import com.project.domain.circle.entity.Circle;
import com.project.domain.usercircle.entity.UserCircle;
import com.project.domain.users.dto.UserDTO;
import com.project.domain.users.entity.Users;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class CircleDTO {

    @Data
    public static class CircleSimpleInfoResponse {
        private Long circleId;
        private String circleName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public CircleSimpleInfoResponse(Circle circle) {
            this.circleId = circle.getId();
            this.circleName = circle.getName();
            this.createdAt = circle.getCreatedAt();
            this.updatedAt = circle.getModifiedAt();
        }
    }

    @Data
    public static class CircleSimpleInfoListResponse {

        private List<CircleSimpleInfoResponse> circleSimpleInfoResponseList;

        public CircleSimpleInfoListResponse(List<CircleSimpleInfoResponse> circleSimpleInfoResponseList) {
            this.circleSimpleInfoResponseList = circleSimpleInfoResponseList;
        }
    }

    @Data
    public static class CircleDetailInfoResponse {
        private Long circleId;
        private String circleName;
        private String imageUrl;
        private String description;
        private Integer userCount;
        private Integer pinCount;
        private Integer pictureCount;
        private List<UserDTO.UserSimpleInfoResponse> joinedUserList;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public CircleDetailInfoResponse(Circle circle) {
            this.circleId = circle.getId();
            this.circleName = circle.getName();
            this.imageUrl = circle.getImageUrl();
            this.description = circle.getDescription();
            this.userCount = circle.getUserCircleList().size();
            this.pinCount = circle.getPinList().size();
            this.pictureCount = circle.getPinList().stream().mapToInt(pl -> pl.getPictures().size()).sum();
            List<UserDTO.UserSimpleInfoResponse> userList = new ArrayList<>();
            circle.getUserCircleList().forEach((uc) -> {
                userList.add(new UserDTO.UserSimpleInfoResponse(uc.getUser()));
            });
            this.joinedUserList = userList;
            this.createdAt = circle.getCreatedAt();
            this.updatedAt = circle.getModifiedAt();
        }
    }

    @Data
    public static class CreateCircleRequest {

        @NotBlank(message = "그룹 이름을 입력해주세요.")
        private String circleName;

        private List<Long> invitedUserList;

        private String imageUrl = "https://cdn-icons-png.flaticon.com/512/149/149071.png?w=1480&t=st=1679211933~exp=1679212533~hmac=b61bdb1145eb754a852d3c13ed5006de6ee4c0b4b0dd6c5f8575e7828f7ff977";

        public Circle toEntity() {
            return Circle.builder()
                    .name(circleName)
                    .imageUrl(imageUrl)
                    .build();
        }
    }

    @Data
    public static class UpdateCircleRequest {

        private String circleName;
        private String description;
    }

    @Data
    public static class CircleWithJoinUserResponse {

        private Long circleId;
        private String circleName;
        private UserDTO.UserSimpleInfoResponse master;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        private List<UserDTO.UserSimpleInfoResponse> joinedUserList;

        public CircleWithJoinUserResponse(Circle circle) {
            List<UserDTO.UserSimpleInfoResponse> userList = new ArrayList<>();
            circle.getUserCircleList().forEach((uc) -> {
                userList.add(new UserDTO.UserSimpleInfoResponse(uc.getUser()));
            });
            this.joinedUserList = userList;
            this.circleId = circle.getId();
            this.circleName = circle.getName();
            this.master = new UserDTO.UserSimpleInfoResponse(circle.getMaster());
            this.createdAt = circle.getCreatedAt();
            this.updatedAt = circle.getModifiedAt();
        }
    }

    @Data
    public static class InviteUserRequest {

        private List<Long> invitedUserList;
    }

    @Data
    public static class InviteUserResponse {

        private Long circleId;
        private List<UserDTO.UserSimpleInfoResponse> userList;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;


        public InviteUserResponse(Circle circle) {
            List<UserDTO.UserSimpleInfoResponse> userList = new ArrayList<>();
            circle.getUserCircleList().forEach((userCircle) -> {
                userList.add(new UserDTO.UserSimpleInfoResponse(userCircle.getUser()));
            });
            this.circleId = circle.getId();
            this.userList = userList;
            this.createdAt = circle.getCreatedAt();
            this.updatedAt = circle.getModifiedAt();
        }
    }

    @Data
    public static class InviteUserFromLinkResponse {
        private Long circleId;
        private UserDTO.UserSimpleInfoResponse user;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public InviteUserFromLinkResponse(Circle circle) {
            this.circleId = circle.getId();
            this.user = new UserDTO.UserSimpleInfoResponse(circle.getUserCircleList().get(0).getUser());
            this.createdAt = circle.getUserCircleList().get(0).getCreatedAt();
            this.updatedAt = circle.getUserCircleList().get(0).getModifiedAt();
        }
    }

    @Data
    public static class BanUserRequest {

        @NotBlank(message = "유저 아이디를 입력해주세요.")
        private Long userId;
    }

    @Data
    public static class UpdateCircleMasterRequest {

        @NotBlank(message = "유저 아이디를 입력해주세요.")
        private Long userId;
    }

    @Data
    public static class AllowUserJoinResponse {

        private Long userId;
        private Long circleId;
        private String userNickname;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public AllowUserJoinResponse(Users user, UserCircle userCircle) {
            this.userId = user.getId();
            this.circleId = userCircle.getCircle().getId();
            this.userNickname = user.getNickname();
            this.createdAt = userCircle.getCreatedAt();
            this.updatedAt = userCircle.getModifiedAt();
        }
    }
}

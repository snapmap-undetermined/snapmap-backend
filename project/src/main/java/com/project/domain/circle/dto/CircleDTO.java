package com.project.domain.circle.dto;


import com.project.domain.circle.entity.Circle;
import com.project.domain.pin.dto.PinDTO;
import com.project.domain.usercircle.entity.UserCircle;
import com.project.domain.users.dto.UserDTO.UserSimpleInfoResponse;
import com.project.domain.users.entity.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class CircleDTO {

    @Data
    public static class CircleSimpleInfoResponse {
        private Long circleId;
        private String circleName;
        private String circleImageUrl;
        private Integer joinedUserCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public CircleSimpleInfoResponse(Circle circle) {
            this.circleId = circle.getId();
            this.circleName = circle.getName();
            this.circleImageUrl = circle.getImageUrl();
            this.joinedUserCount = circle.getUserCircleList().size();
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
        private List<UserSimpleInfoResponse> joinedUserList;
        private PinDTO.PinDetailListResponse pinList;
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
            this.joinedUserList = circle.getUserCircleList().stream().map(uc -> new UserSimpleInfoResponse(uc.getUser())).collect(Collectors.toList());
            this.pinList = new PinDTO.PinDetailListResponse(circle.getPinList().stream()
                    .map(PinDTO.PinDetailResponse::new)
                    .collect(Collectors.toList()));
            this.createdAt = circle.getCreatedAt();
            this.updatedAt = circle.getModifiedAt();
        }
    }

    @Data
    public static class CreateCircleRequest {

        @NotBlank(message = "그룹 이름을 입력해주세요.")
        private String circleName;
        private String description;
        private List<Long> invitedUserList;
        private String imageUrl = "https://cdn-icons-png.flaticon.com/512/149/149071.png?w=1480&t=st=1679211933~exp=1679212533~hmac=b61bdb1145eb754a852d3c13ed5006de6ee4c0b4b0dd6c5f8575e7828f7ff977";

        public Circle toEntity() {
            return Circle.builder()
                    .name(circleName)
                    .description(description)
                    .imageUrl(imageUrl)
                    .build();
        }
    }

    @Data
    public static class UpdateCircleRequest {
        @NotBlank(message = "그룹 이름을 입력해주세요.")
        private String circleName;
        private String description;
    }

    @Data
    public static class CircleWithJoinUserResponse {

        private Long circleId;
        private String circleName;
        private String description;
        private String circleImageUrl;
        private UserSimpleInfoResponse master;
        private List<UserSimpleInfoResponse> joinedUserList;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public CircleWithJoinUserResponse(Circle circle) {
            this.circleId = circle.getId();
            this.circleName = circle.getName();
            this.description = circle.getDescription();
            this.circleImageUrl = circle.getImageUrl();
            this.master = new UserSimpleInfoResponse(circle.getMaster());
            this.joinedUserList = circle.getUserCircleList().stream()
                    .filter(UserCircle::getActivated)
                    .map(UserCircle::getUser)
                    .filter(user -> !user.equals(circle.getMaster()))
                    .map(UserSimpleInfoResponse::new)
                    .collect(Collectors.toList());
            this.createdAt = circle.getCreatedAt();
            this.updatedAt = circle.getModifiedAt();
        }
    }

    @Data
    public static class InviteUserRequest {
        @Size(min = 1, message = "한 명 이상의 유저를 선택해야 합니다.")
        private List<Long> invitedUserList;
    }

    @Data
    public static class InviteUserResponse {

        private Long circleId;
        private Long userId;
        private String userNickname;
        private List<UserSimpleInfoResponse> userList;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;


        public InviteUserResponse(Circle circle, Users user) {
            this.circleId = circle.getId();
            this.userId = user.getId();
            this.userNickname = user.getNickname();
            this.userList = circle.getUserCircleList().stream()
                    .filter(userCircle -> !userCircle.getActivated())
                    .map((uc) -> new UserSimpleInfoResponse(uc.getUser()))
                    .collect(Collectors.toList());
            this.createdAt = circle.getCreatedAt();
            this.updatedAt = circle.getModifiedAt();
        }
    }

    @Data
    public static class InviteUserFromLinkResponse {
        private Long circleId;
        private UserSimpleInfoResponse user;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public InviteUserFromLinkResponse(UserCircle userCircle) {
            this.circleId = userCircle.getCircle().getId();
            this.user = new UserSimpleInfoResponse(userCircle.getUser());
            this.createdAt = userCircle.getCreatedAt();
            this.updatedAt = userCircle.getModifiedAt();
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
    public static class acceptCircleInvitationResponse {
        private Long userId;
        private Long circleId;
        private Boolean activated;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public acceptCircleInvitationResponse(Users user, UserCircle userCircle) {
            this.userId = userCircle.getUser().getId();
            this.circleId = userCircle.getCircle().getId();
            this.activated = userCircle.getActivated();
            this.createdAt = userCircle.getCreatedAt();
            this.updatedAt = userCircle.getModifiedAt();
        }
    }

    @Data
    public static class NotAcceptCircleInviteUserResponse {

        private List<UserSimpleInfoResponse> userList;

        public NotAcceptCircleInviteUserResponse(Circle circle) {

            this.userList = circle.getUserCircleList().stream()
                    .filter(userCircle -> !userCircle.getActivated())
                    .map((uc) -> new UserSimpleInfoResponse(uc.getUser()))
                    .collect(Collectors.toList());
        }
    }

    @Data
    public static class cancelInviteCircleResponse {
        private Long userId;
        private Long circleId;
        private LocalDateTime updatedAt;

        public cancelInviteCircleResponse(Users user, Circle circle) {
            this.userId = user.getId();
            this.circleId = circle.getId();
            this.updatedAt = LocalDateTime.now();

        }
    }

}

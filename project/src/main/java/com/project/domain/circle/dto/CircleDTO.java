package com.project.domain.circle.dto;


import com.project.domain.circle.entity.Circle;
import com.project.domain.usercircle.entity.UserCircle;
import com.project.domain.users.dto.UserDTO;
import com.project.domain.users.entity.Users;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
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
    @Builder
    public static class CreateCircleRequest {

        @NotBlank(message = "그룹이름을 입력해주세요.")
        private String circleName;

        public Circle toEntity() {
            return Circle.builder()
                    .name(circleName)
                    .build();
        }
    }

    @Data
    public static class UpdateCircleRequest {

        @NotBlank(message = "그룹이름을 입력해주세요.")
        private String circleName;
        private String imageUrl;
    }

    @Data
    public static class CircleWithJoinUserResponse {

        private Long circleId;
        private String circleName;

        private List<UserDTO.UserSimpleInfoResponse> joinedUserList;

        public CircleWithJoinUserResponse(List<Users> userList, Circle circle) {
            this.joinedUserList = userList.stream().map(UserDTO.UserSimpleInfoResponse::new).collect(Collectors.toList());
            this.circleId = circle.getId();
            this.circleName = circle.getName();
        }
    }

    @Data
    public static class InviteCircleRequest {

        @NotBlank(message = "circleId를 입력해주세요.")
        private Long circleId;

        private List<Users> users;

        public InviteCircleRequest(UserCircle userCircle) {
            this.circleId = userCircle.getCircle().getId();
        }

        public UserCircle toEntity(Users user, Circle circle) {
            return UserCircle.builder()
                    .user(user)
                    .circle(circle)
                    .status(0)
                    .build();
        }
    }

    @Data
    public static class InviteCircleResponse {

        private Long userId;
        private Long circleId;
        private List<UserDTO.UserSimpleInfoResponse> users;

        public InviteCircleResponse(UserCircle userCircle) {
            this.userId = userCircle.getUser().getId();
            this.circleId = userCircle.getCircle().getId();
            this.users = userCircle.getCircle().getUserCircleList().stream().map((uc) -> new UserDTO.UserSimpleInfoResponse(uc.getUser())).toList();
        }

    }

    @Data
    public static class ExpulsionUserRequest {

        @NotBlank(message = "userId 입력해주세요.")
        private Long userId;

        public ExpulsionUserRequest(Long userId) {
            this.userId = userId;
        }

    }

    @Data
    public static class AllowJoinCircleResponse {

        private Long userId;
        private Long circleId;
        private String userNickname;

        public AllowJoinCircleResponse(Users user, UserCircle userCircle) {
            this.userId = user.getId();
            this.circleId = userCircle.getCircle().getId();
            this.userNickname = user.getNickname();
        }
    }


}

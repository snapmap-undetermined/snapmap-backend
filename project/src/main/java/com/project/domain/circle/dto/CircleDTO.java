package com.project.domain.circle.dto;


import com.project.domain.circle.entity.Circle;
import com.project.domain.usercircle.entity.UserCircle;
import com.project.domain.users.dto.UserDTO;
import com.project.domain.users.entity.Users;
import jakarta.validation.constraints.NotBlank;
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
    public static class JoinCircleRequest {

        @NotBlank(message = "circleId를 입력해주세요.")
        private Long circleId;

        public JoinCircleRequest(UserCircle userCircle) {
            this.circleId = userCircle.getCircle().getId();
        }

        public UserCircle toEntity(Users user, Circle circle) {
            return UserCircle.builder()
                    .user(user)
                    .circle(circle)
                    .build();
        }
    }

    @Data
    public static class JoinCircleResponse {

        private Long userId;
        private String userNickname;
        private Long circleId;

        public JoinCircleResponse(UserCircle userCircle) {
            this.userId = userCircle.getUser().getId();
            this.userNickname = userCircle.getUser().getNickname();
            this.circleId = userCircle.getCircle().getId();
        }

    }


}

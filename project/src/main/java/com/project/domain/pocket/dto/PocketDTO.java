package com.project.domain.pocket.dto;


import com.project.domain.pocket.entity.Pocket;
import com.project.domain.pin.dto.PinDTO;
import com.project.domain.userpocket.entity.UserPocket;
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
public class PocketDTO {

    @Data
    public static class PocketSimpleInfoResponse {
        private Long pocketId;
        private String pocketName;
        private String pocketImageUrl;
        private Integer joinedUserCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public PocketSimpleInfoResponse(Pocket pocket) {
            this.pocketId = pocket.getId();
            this.pocketName = pocket.getName();
            this.pocketImageUrl = pocket.getImageUrl();
            this.joinedUserCount = pocket.getUserPocketList().size();
            this.createdAt = pocket.getCreatedAt();
            this.updatedAt = pocket.getModifiedAt();
        }
    }

    @Data
    public static class PocketSimpleInfoListResponse {
        private List<PocketSimpleInfoResponse> pocketSimpleInfoResponseList;

        public PocketSimpleInfoListResponse(List<PocketSimpleInfoResponse> pocketSimpleInfoResponseList) {
            this.pocketSimpleInfoResponseList = pocketSimpleInfoResponseList;
        }
    }

    @Data
    public static class PocketDetailInfoResponse {
        private Long pocketId;
        private String pocketName;
        private String imageUrl;
        private String description;
        private Integer userCount;
        private Integer pinCount;
        private Integer pictureCount;
        private List<UserSimpleInfoResponse> joinedUserList;
        private PinDTO.PinDetailListResponse pinList;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public PocketDetailInfoResponse(Pocket pocket) {
            this.pocketId = pocket.getId();
            this.pocketName = pocket.getName();
            this.imageUrl = pocket.getImageUrl();
            this.description = pocket.getDescription();
            this.userCount = pocket.getUserPocketList().size();
            this.pinCount = pocket.getPinList().size();
            this.pictureCount = pocket.getPinList().stream().mapToInt(pl -> pl.getPictures().size()).sum();
            this.joinedUserList = pocket.getUserPocketList().stream().map(uc -> new UserSimpleInfoResponse(uc.getUser())).collect(Collectors.toList());
            this.pinList = new PinDTO.PinDetailListResponse(pocket.getPinList().stream()
                    .map(PinDTO.PinDetailResponse::new)
                    .collect(Collectors.toList()));
            this.createdAt = pocket.getCreatedAt();
            this.updatedAt = pocket.getModifiedAt();
        }
    }

    @Data
    public static class CreatePocketRequest {

        @NotBlank(message = "포켓 이름을 입력해주세요.")
        private String pocketName;
        private String description;
        private List<Long> invitedUserList;
        private String imageUrl = "https://cdn-icons-png.flaticon.com/512/149/149071.png?w=1480&t=st=1679211933~exp=1679212533~hmac=b61bdb1145eb754a852d3c13ed5006de6ee4c0b4b0dd6c5f8575e7828f7ff977";

        public Pocket toEntity() {
            return Pocket.builder()
                    .name(pocketName)
                    .description(description)
                    .imageUrl(imageUrl)
                    .build();
        }
    }

    @Data
    public static class UpdatePocketRequest {
        @NotBlank(message = "포켓 이름을 입력해주세요.")
        private String pocketName;
        private String description;
    }

    @Data
    public static class PocketWithJoinUserResponse {

        private Long pocketId;
        private String pocketName;
        private String description;
        private String pocketImageUrl;
        private UserSimpleInfoResponse master;
        private List<UserSimpleInfoResponse> joinedUserList;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public PocketWithJoinUserResponse(Pocket pocket) {
            this.pocketId = pocket.getId();
            this.pocketName = pocket.getName();
            this.description = pocket.getDescription();
            this.pocketImageUrl = pocket.getImageUrl();
            this.master = new UserSimpleInfoResponse(pocket.getMaster());
            this.joinedUserList = pocket.getUserPocketList().stream()
                    .filter(UserPocket::getActivated)
                    .map(UserPocket::getUser)
                    .filter(user -> !user.equals(pocket.getMaster()))
                    .map(UserSimpleInfoResponse::new)
                    .collect(Collectors.toList());
            this.createdAt = pocket.getCreatedAt();
            this.updatedAt = pocket.getModifiedAt();
        }
    }

    @Data
    public static class InviteUserRequest {
        @Size(min = 1, message = "한 명 이상의 유저를 선택해야 합니다.")
        private List<Long> invitedUserList;
    }

    @Data
    public static class InviteUserResponse {

        private Long pocketId;
        private Long userId;
        private String userNickname;
        private List<UserSimpleInfoResponse> userList;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;


        public InviteUserResponse(Pocket pocket, Users user) {
            this.pocketId = pocket.getId();
            this.userId = user.getId();
            this.userNickname = user.getNickname();
            this.userList = pocket.getUserPocketList().stream()
                    .filter(userPocket -> !userPocket.getActivated())
                    .map((up) -> new UserSimpleInfoResponse(up.getUser()))
                    .collect(Collectors.toList());
            this.createdAt = pocket.getCreatedAt();
            this.updatedAt = pocket.getModifiedAt();
        }
    }

    @Data
    public static class InviteUserFromLinkResponse {
        private Long pocketId;
        private UserSimpleInfoResponse user;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public InviteUserFromLinkResponse(UserPocket userPocket) {
            this.pocketId = userPocket.getPocket().getId();
            this.user = new UserSimpleInfoResponse(userPocket.getUser());
            this.createdAt = userPocket.getCreatedAt();
            this.updatedAt = userPocket.getModifiedAt();
        }
    }

    @Data
    public static class BanUserRequest {

        @NotBlank(message = "유저 아이디를 입력해주세요.")
        private Long userId;
    }

    @Data
    public static class UpdatePocketMasterRequest {

        @NotBlank(message = "유저 아이디를 입력해주세요.")
        private Long userId;
    }

    @Data
    public static class acceptPocketInvitationResponse {
        private Long userId;
        private Long pocketId;
        private Boolean activated;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public acceptPocketInvitationResponse(Users user, UserPocket userPocket) {
            this.userId = userPocket.getUser().getId();
            this.pocketId = userPocket.getPocket().getId();
            this.activated = userPocket.getActivated();
            this.createdAt = userPocket.getCreatedAt();
            this.updatedAt = userPocket.getModifiedAt();
        }
    }

    @Data
    public static class NotAcceptPocketInviteUserResponse {

        private List<UserSimpleInfoResponse> userList;

        public NotAcceptPocketInviteUserResponse(Pocket pocket) {

            this.userList = pocket.getUserPocketList().stream()
                    .filter(userPocket -> !userPocket.getActivated())
                    .map((up) -> new UserSimpleInfoResponse(up.getUser()))
                    .collect(Collectors.toList());
        }
    }

    @Data
    public static class cancelInvitePocketResponse {
        private Long userId;
        private Long pocketId;
        private LocalDateTime updatedAt;

        public cancelInvitePocketResponse(Users user, Pocket pocket) {
            this.userId = user.getId();
            this.pocketId = pocket.getId();
            this.updatedAt = LocalDateTime.now();

        }
    }

}

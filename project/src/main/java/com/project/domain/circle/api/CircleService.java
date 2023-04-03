package com.project.domain.circle.api;

import com.project.domain.circle.dto.CircleDTO;
import com.project.domain.users.entity.Users;
import org.springframework.web.multipart.MultipartFile;

public interface CircleService {

    CircleDTO.CircleSimpleInfoResponse createCircle(Users user, CircleDTO.CreateCircleRequest createCircleRequest);

    CircleDTO.CircleSimpleInfoListResponse getAllCircleByUser(Long userId);

    CircleDTO.CircleDetailInfoResponse getCircleDetail(Long circleId);

    CircleDTO.CircleWithJoinUserResponse getJoinedUserOfCircle(Long circleId) throws Exception;

    CircleDTO.CircleSimpleInfoResponse leaveCircle(Users user, Long circleId) throws Exception;

    CircleDTO.CircleSimpleInfoResponse banUserFromCircle(Users user, Long circleId, CircleDTO.BanUserRequest banUserRequest);

    CircleDTO.InviteUserResponse inviteUser(Users user, Long circleId, CircleDTO.InviteUserRequest request);

    CircleDTO.InviteUserFromLinkResponse inviteUserFromLink(Users user, String circleKey);

    CircleDTO.acceptCircleInvitationResponse acceptCircleInvitation(Users user, Long circleId);

    CircleDTO.cancelInviteCircleResponse cancelCircleInvitation(Users user, Long circleId, Long cancelUserId);

    CircleDTO.CircleSimpleInfoResponse updateCircle(Users user, Long circleId, CircleDTO.UpdateCircleRequest request, MultipartFile picture);

    CircleDTO.CircleWithJoinUserResponse updateCircleMaster(Users user, Long circleId, Long userId);

    CircleDTO.NotAcceptCircleInviteUserResponse getAllNotAcceptCircleInviteUser(Users user, Long circleId);

}

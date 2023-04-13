package com.project.domain.pocket.api;

import com.project.domain.pocket.dto.PocketDTO;
import com.project.domain.users.entity.Users;
import org.springframework.web.multipart.MultipartFile;

public interface PocketService {

    PocketDTO.PocketSimpleInfoResponse createPocket(Users user, PocketDTO.CreatePocketRequest createPocketRequest);

    PocketDTO.PocketSimpleInfoListResponse getAllPocketByUser(Long userId);

    PocketDTO.PocketDetailInfoResponse getPocketDetail(Long pocketId);

    PocketDTO.PocketWithJoinUserResponse getJoinedUserOfPocket(Long pocketId) throws Exception;

    PocketDTO.PocketSimpleInfoResponse leavePocket(Users user, Long pocketId) throws Exception;

    PocketDTO.PocketSimpleInfoResponse banUserFromPocket(Users user, Long pocketId, PocketDTO.BanUserRequest banUserRequest);

    PocketDTO.InviteUserResponse inviteUser(Users user, Long pocketId, PocketDTO.InviteUserRequest request);

    PocketDTO.InviteUserFromLinkResponse inviteUserFromLink(Users user, String pocketKey);

    PocketDTO.acceptPocketInvitationResponse acceptPocketInvitation(Users user, Long pocketId);

    PocketDTO.cancelInvitePocketResponse cancelPocketInvitation(Users user, Long pocketId, Long cancelUserId);

    PocketDTO.PocketSimpleInfoResponse updatePocket(Users user, Long pocketId, PocketDTO.UpdatePocketRequest request, MultipartFile picture);

    PocketDTO.PocketWithJoinUserResponse updatePocketMaster(Users user, Long pocketId, Long userId);

    PocketDTO.NotAcceptPocketInviteUserResponse getAllNotAcceptPocketInviteUser(Users user, Long pocketId);

}

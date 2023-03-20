package com.project.domain.circle.api;

import com.project.domain.circle.dto.CircleDTO;
import com.project.domain.circle.entity.Circle;
import com.project.domain.users.entity.Users;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CircleService {

    CircleDTO.CircleSimpleInfoResponse createCircle(Users user, CircleDTO.CreateCircleRequest createCircleRequest);

    CircleDTO.CircleSimpleInfoListResponse getAllCircleByUser(Long userId);

    CircleDTO.CircleDetailInfoResponse getCircle(Long circleId);

    CircleDTO.CircleWithJoinUserResponse getJoinedUserOfCircle(Long circleId) throws Exception;

    CircleDTO.CircleSimpleInfoResponse leaveCircle(Users user, Long circleId) throws Exception;

    CircleDTO.CircleSimpleInfoResponse banUserFromCircle(Users user, Long circleId, CircleDTO.BanUserRequest banUserRequest);

    CircleDTO.InviteUserResponse inviteUser(Users user, Long circleId, CircleDTO.InviteUserRequest request);

    CircleDTO.AllowUserJoinResponse allowUserJoin(Users user, Long circleId);

    CircleDTO.CircleSimpleInfoResponse updateCircle(Users user, Long circleId, CircleDTO.UpdateCircleRequest request, MultipartFile picture);


}

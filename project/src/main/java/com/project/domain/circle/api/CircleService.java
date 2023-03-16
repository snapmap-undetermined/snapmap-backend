package com.project.domain.circle.api;

import com.project.domain.circle.dto.CircleDTO;
import com.project.domain.users.entity.Users;

import java.util.List;

public interface CircleService {

    CircleDTO.CircleSimpleInfoResponse createCircle(Users user, CircleDTO.CreateCircleRequest createCircleRequest);

    CircleDTO.CircleSimpleInfoListResponse getCircleListByUser(Long userId);

    CircleDTO.CircleWithJoinUserResponse getUserListByCircle(Long circleId) throws Exception;

    CircleDTO.CircleSimpleInfoResponse leaveCircle(Long userId, Long circleId) throws Exception;

    CircleDTO.CircleSimpleInfoResponse expulsionUserFromCircle(Long userId, Long circleId, CircleDTO.ExpulsionUserRequest expulsionUserRequest);

    CircleDTO.InviteCircleResponse inviteCircle(Users user, Long circleId, CircleDTO.InviteCircleRequest request);

    CircleDTO.AllowJoinCircleResponse allowJoinCircle(Users user, Long circleId);

    CircleDTO.CircleSimpleInfoResponse updateCircle(Long userId, Long circleId, CircleDTO.UpdateCircleRequest request);


}

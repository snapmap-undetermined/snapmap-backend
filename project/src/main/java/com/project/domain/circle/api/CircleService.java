package com.project.domain.circle.api;

import com.project.domain.circle.dto.CircleDTO;
import com.project.domain.users.entity.Users;

import java.util.List;

public interface CircleService {

    CircleDTO.CircleSimpleResponse createCircle(Users user, CircleDTO.CreateCircleRequest createCircleRequest);

    CircleDTO.CircleSimpleInfoListResponse getAllCircleByUser(Long userId);

    CircleDTO.CircleDetailResponse getCircleDetail(Long circleId);

    CircleDTO.CircleWithJoinUserResponse getAllUserByCircle(Long circleId) throws Exception;

    Long leaveCircle(Long userId, Long circleId) throws Exception;

    CircleDTO.JoinCircleResponse joinCircle(Users user, CircleDTO.JoinCircleRequest request);

    CircleDTO.CircleSimpleResponse updateCircleName(Long circleId, CircleDTO.UpdateCircleRequest request);


}

package com.project.album.domain.circle.api;

import com.project.album.domain.circle.dto.CircleDTO;
import com.project.album.domain.circle.entity.Circle;
import com.project.album.domain.usercircle.dto.UserCircleDTO;
import com.project.album.domain.users.dto.UserDTO;
import com.project.album.domain.users.entity.Users;

import java.util.List;

public interface CircleService {

    CircleDTO.CircleSimpleInfoResponse createCircle(Users user, CircleDTO.CreateCircleRequest createCircleRequest);

    List<CircleDTO.CircleSimpleInfoResponse> getCircleListByUser(Long userId);

    CircleDTO.CircleWithJoinUserResponse getUserListByCircle(Long userId, Long circleId) throws Exception;

    int leaveCircle(Long userId, Long circleId) throws Exception;

    CircleDTO.JoinCircleResponse joinCircle(Users user, CircleDTO.JoinCircleRequest request);

    CircleDTO.CircleSimpleInfoResponse updateCircleName(Long circleId, CircleDTO.UpdateCircleRequest request);


}

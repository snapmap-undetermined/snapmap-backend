package com.project.album.domain.circle.api;

import com.project.album.domain.circle.dto.CircleDTO;
import com.project.album.domain.circle.entity.Circle;
import com.project.album.domain.usercircle.dto.UserCircleDTO;
import com.project.album.domain.users.dto.UserDTO;
import com.project.album.domain.users.entity.Users;

import java.util.List;

public interface CircleService {

    CircleDTO.CircleSimpleInfoResponse createCircle(Users user, CircleDTO.CreateCircleRequest createCircleRequest) throws Exception;

    List<CircleDTO.CircleSimpleInfoResponse> getCircleListByUser(Long userId) throws Exception;

    List<UserDTO.UserSimpleInfoResponse> getUserListByCircle(Long userId, Long circleId) throws Exception;

    int exitedUser(Long userId, Long circleId) throws Exception;

    UserCircleDTO.UserCircleSimpleInfoResponse joinCircle(Users user, UserCircleDTO.JoinUserCircleRequest request) throws Exception;

    CircleDTO.CircleSimpleInfoResponse updateCircleName(Long circleId, CircleDTO.UpdateCircleRequest request) throws Exception;


}

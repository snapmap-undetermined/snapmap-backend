package com.project.domain.users.api;

import com.project.common.exception.EntityNotFoundException;
import com.project.domain.circle.entity.Circle;
import com.project.domain.users.api.interfaces.UserService;
import com.project.domain.users.dto.UserDTO;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public UserDTO.UserSimpleInfoResponse getUserByNickname(String nickname) {

        Users user = userRepository.findByNickname(nickname).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자 입니다."));

        return new UserDTO.UserSimpleInfoResponse(user);
    }

    @Override
    @Transactional
    public UserDTO.UserSimpleInfoResponse updateUser(Long userId, UserDTO.UpdateUserRequest request) {
        Users user = getUser(userId);
        user.setNickname(request.getNickname());
        user.setProfileImage(request.getProfileImage());
        return new UserDTO.UserSimpleInfoResponse(user);
    }

    @Override
    public void deleteUser(Users users) {
        Users user = getUser(users.getId());
        userRepository.delete(user);
    }

    private Users getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자 입니다."));
    }
}

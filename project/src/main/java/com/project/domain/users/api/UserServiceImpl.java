package com.project.domain.users.api;

import com.project.common.exception.EntityNotFoundException;
import com.project.domain.users.api.interfaces.UserService;
import com.project.domain.users.dto.UserDTO;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO.UserSimpleInfoResponse getUserByNickname(String nickname) {
        Users user = userRepository.findByNickname(nickname).orElse(null);
        if (user == null) {
            log.info("user nickname : {} does not exist", nickname);
            throw new EntityNotFoundException("User does not exist.");
        }
        return new UserDTO.UserSimpleInfoResponse(user);
    }

    @Override
    @Transactional
    public UserDTO.UserSimpleInfoResponse updateUser(Long userId, UserDTO.UpdateUserRequest request) {
        Users user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            log.info("userId : {} does not exist", userId);
            throw new EntityNotFoundException("User does not exist.");
        }
        if (request.getNickname() != null) {
            log.info("user nickname updated {} -> {}", user.getNickname(), request.getNickname());
            user.updateNickname(request.getNickname());
        }
        if (request.getProfileImage() != null) {
            user.updateProfileImage(request.getProfileImage());
        }
        return new UserDTO.UserSimpleInfoResponse(user);
    }

    @Override
    public void deleteUser(Users user) {
        Users targetUser = userRepository.findById(user.getId()).orElse(null);
        if (targetUser == null) {
            log.info("userId : {} does not exist", user.getId());
            throw new EntityNotFoundException("User does not exist.");
        }
        userRepository.delete(user);
    }
}

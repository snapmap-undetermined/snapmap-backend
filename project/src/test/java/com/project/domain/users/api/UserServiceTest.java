package com.project.domain.users.api;

import com.project.common.TestObjectFactory;
import com.project.domain.users.dto.UserDTO;
import com.project.domain.users.dto.UserDTO.UpdateUserRequest;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import com.project.domain.users.repository.UserRepositoryCustom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("유저의 닉네임을 업데이트할 수 있다.")
    public void update_user_nickname() {
        UpdateUserRequest updateUserRequest = TestObjectFactory.createUpdateUserRequest("AFTER", "ORIGINAL.JPEG");
        when(userRepository.findById(anyLong())).thenAnswer(x -> Optional.of(Users.builder().email("TEST@EMAIL.COM").nickname("BEFORE").password("PASSWORD").build()));

        UserDTO.UserSimpleInfoResponse response = userService.updateUser(-1L, updateUserRequest);

        Assertions.assertEquals("AFTER", response.getUserNickname());
    }
}

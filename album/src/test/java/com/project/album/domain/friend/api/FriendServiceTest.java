package com.project.album.domain.friend.api;

import com.project.album.common.entity.Role;
import com.project.album.domain.friend.dto.FriendDTO;
import com.project.album.domain.friend.entity.Friend;
import com.project.album.domain.friend.repository.FriendRepository;
import com.project.album.domain.users.entity.Users;
import com.project.album.domain.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FriendServiceTest {

    @InjectMocks
    FriendServiceImpl friendService;

    @Mock
    FriendRepository friendRepository;

    @Mock
    UserRepository userRepository;

    Users meUser;
    Users friendUser;

    @BeforeEach
    @DisplayName("테스트에 필요한 me user객체와 friend user객체를 미리 생성해 놓는다.")
    void initUseCase() {
        meUser = Users.builder().email("kc@a.com").nickname("kc").password("123").role(Role.USER).build();
        friendUser = Users.builder().email("sh@a.com").nickname("sh").password("123").role(Role.USER).build();
        userRepository.save(meUser);
        userRepository.save(friendUser);
    }

    @Test
    void getAllFriendsByUser() throws Exception {

        List<FriendDTO.FriendSimpleInfoResponse> friendSimpleInfoResponseList = friendRepository.findByUserId(meUser.getId()).stream().map(FriendDTO.FriendSimpleInfoResponse::new).collect(Collectors.toList());

        when(friendService.getAllFriends(meUser.getId())).thenReturn(friendSimpleInfoResponseList);

        assertThat(friendService.getAllFriends(meUser.getId())).isEqualTo(friendSimpleInfoResponseList);
    }
}

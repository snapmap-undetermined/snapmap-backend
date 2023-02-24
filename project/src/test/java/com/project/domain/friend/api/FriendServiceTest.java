package com.project.domain.friend.api;

import com.project.common.entity.Role;
import com.project.domain.friend.dto.FriendDTO;
import com.project.domain.friend.entity.Friend;
import com.project.domain.friend.repository.FriendRepository;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
    @DisplayName("userId로 friend의 리스트를 가져온다.")
    void getAllFriendsByUser() throws Exception {

        //given
        List<Friend> friendList = new ArrayList<>();
        Friend friend = Friend.builder().friend(friendUser).me(meUser).friendName("testFriend").build();
        friendRepository.save(friend);
        friendList.add(friend);
        given(friendRepository.findByUserId(meUser.getId())).willReturn(friendList);

        //when
        final FriendDTO.FriendSimpleInfoResponse friendSimpleInfoResponse = friendService.getAllFriends(meUser.getId()).get(0);

        //then
        assertThat(friendSimpleInfoResponse.getFriendUserId()).isEqualTo(friendUser.getId());
    }

    @Test
    void createFriend() throws Exception {

        //given
        List<Friend> friendList = new ArrayList<>();
        Friend friend = Friend.builder().friend(friendUser).me(meUser).friendName("testFriend").build();
        friendRepository.save(friend);

        given(friendRepository.save(friend)).willReturn(friend);
        //mocking

        //when
//        final FriendDTO.FriendSimpleInfoResponse friendSimpleInfoResponse = friendService.createFriend(meUser,)
        //then
    }
}

package com.project.domain.friend.api;

import com.project.common.TestObjectFactory;
import com.project.common.exception.BusinessLogicException;
import com.project.common.exception.EntityNotFoundException;
import com.project.common.exception.ErrorCode;
import com.project.common.exception.InvalidValueException;
import com.project.domain.friend.dto.FriendDTO;
import com.project.domain.friend.entity.Friend;
import com.project.domain.friend.repository.FriendRepository;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FriendServiceTest {

    @InjectMocks
    private FriendServiceImpl friendService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FriendRepository friendRepository;

    @Test
    @DisplayName("새로운 친구를 추가할 수 있다.")
    public void create_new_friend() {
        Users me = TestObjectFactory.createUser(111L, "EMAIL1@EXAMPLE.COM", "ME", "PASSWORD");
        FriendDTO.CreateFriendRequest friendRequest = TestObjectFactory.createFriendRequest(999L);
        when(userRepository.findById(anyLong())).thenAnswer(x -> Optional.of(TestObjectFactory.createUser(999L, "EMAIL2@EXAMPLE.COM", "MATE", "PASSWORD")));
        when(friendRepository.existsByUserIds(anyLong(), anyLong())).thenReturn(false);

        FriendDTO.FriendResponse response = friendService.createFriend(me, friendRequest);

        verify(friendRepository, times(1)).save(any(Friend.class));
        Assertions.assertEquals(999L, response.getMateId());
        Assertions.assertEquals(111L, response.getMeId());
    }

    @Test
    @DisplayName("이미 친구 관계인 친구에 대해 친구 추가 시 예외가 발생한다.")
    public void create_already_friends_raise_exception() {
        Users me = TestObjectFactory.createUser(111L, "EMAIL1@EXAMPLE.COM", "ME", "PASSWORD");
        FriendDTO.CreateFriendRequest friendRequest = TestObjectFactory.createFriendRequest(999L);
        when(userRepository.findById(anyLong())).thenAnswer(x -> Optional.of(TestObjectFactory.createUser(999L, "EMAIL2@EXAMPLE.COM", "MATE", "PASSWORD")));
        when(friendRepository.existsByUserIds(anyLong(), anyLong())).thenReturn(true);

        BusinessLogicException exception = Assertions.assertThrows(BusinessLogicException.class, () -> friendService.createFriend(me, friendRequest));

        Assertions.assertEquals(ErrorCode.FRIEND_DUPLICATION, exception.getErrorCode());
    }

    @Test
    @DisplayName("존재하지 않는 유저에 대해 친구 추가 시 예외가 발생한다.")
    public void create_nonExist_user_as_friend_raise_exception() {
        Users me = TestObjectFactory.createUser(111L, "EMAIL1@EXAMPLE.COM", "ME", "PASSWORD");
        FriendDTO.CreateFriendRequest friendRequest = TestObjectFactory.createFriendRequest(999L);
        when(userRepository.findById(anyLong())).thenAnswer(x -> Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> friendService.createFriend(me, friendRequest));
    }
}

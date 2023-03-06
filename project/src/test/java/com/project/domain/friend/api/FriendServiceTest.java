package com.project.domain.friend.api;

import com.project.common.exception.BusinessLogicException;
import com.project.common.exception.EntityNotFoundException;
import com.project.domain.friend.dto.FriendDTO;
import com.project.domain.friend.entity.Friend;
import com.project.domain.friend.repository.FriendRepository;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class FriendServiceTest {

    private final FriendRepository friendRepository;
    private final FriendService friendService;
    private final UserRepository userRepository;

    Users meUser;
    Users friend1User;

    @Autowired
    public FriendServiceTest(FriendRepository friendRepository, FriendService friendService, UserRepository userRepository) {
        this.friendService = friendService;
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    public void createInitUsers() {
        meUser = Users.builder().email("skc@aaa.com").nickname("skc").password("123").build();
        friend1User = Users.builder().email("jsh@aaa.com").nickname("jsh").password("123").build();
        userRepository.save(meUser);
        userRepository.save(friend1User);
    }

    @BeforeEach
    public void initUseCase() {
        createInitUsers();
    }

    @Test
    @DisplayName("올바른 요청이 들어왔을 때 친구가 정상적으로 생성된다.")
    public void create_friend_with_valid_request_success() throws Exception {

        FriendDTO.CreateFriendRequest request = new FriendDTO.CreateFriendRequest(friend1User);

        friendService.createFriend(meUser, request);

        assertEquals(1, friendRepository.findAll().size());
    }

    @Test
    @DisplayName("요청에 같은 유저의 정보가 담겨 있을 경우에 에러가 발생한다.")
    public void create_friend_has_same_user_id_fail() {

        FriendDTO.CreateFriendRequest request = new FriendDTO.CreateFriendRequest(meUser);

        Throwable exception = assertThrows(BusinessLogicException.class, () -> {
            friendService.createFriend(meUser, request);
        });

        assertEquals("유저-친구 정보가 동일합니다.", exception.getMessage());

    }

    @Test
    @DisplayName("이미 존재하는 친구관계일 경우에 에러가 발생한다.")
    public void create_friend_already_exist_fail() throws Exception {

        FriendDTO.CreateFriendRequest request = new FriendDTO.CreateFriendRequest(friend1User);
        friendService.createFriend(meUser, request);

        Throwable exception = assertThrows(BusinessLogicException.class, () -> {
            friendService.createFriend(meUser, request);
        });

        assertEquals("이미 있는 친구관계 입니다.", exception.getMessage());

    }

    @Test
    @DisplayName("유저 아이디로 유저의 친구관계를 모두 조회할 수 있다.")
    public void get_all_friends_with_user_id_success() throws Exception {

        Users friend2User = Users.builder().email("jsh@aaa.com").nickname("jsh").password("123").build();
        userRepository.save(friend2User);
        FriendDTO.CreateFriendRequest request1 = new FriendDTO.CreateFriendRequest(friend1User);
        FriendDTO.CreateFriendRequest request2 = new FriendDTO.CreateFriendRequest(friend2User);
        friendService.createFriend(meUser, request1);
        friendService.createFriend(meUser, request2);

        List<FriendDTO.FriendSimpleInfoResponse> response = friendService.getAllFriends(meUser.getId());

        assertEquals(2, response.size());
    }

    @Test
    @DisplayName("임의로 친구의 닉네임을 수정할 수 있다.")
    public void update_friend_name_success() throws Exception {

        FriendDTO.CreateFriendRequest createFriendRequest = new FriendDTO.CreateFriendRequest(friend1User);
        FriendDTO.FriendSimpleInfoResponse friend = friendService.createFriend(meUser, createFriendRequest);
        FriendDTO.UpdateFriendNameRequest updateFriendNameRequest = new FriendDTO.UpdateFriendNameRequest("friend1User닉네임을Test로바꿈");
        friendService.updateFriendName(friend.getFriendId(), updateFriendNameRequest);

        Friend updateFriend = friendRepository.findById(friend.getFriendId()).orElseThrow();

        assertEquals("friend1User닉네임을Test로바꿈", updateFriend.getFriendName());
    }

    @Test
    @DisplayName("빈문자열 및 null값으로 친구의 닉네임을 수정할 수 없다.")
    public void update_friend_with_not_exist_friend_id_fail() throws Exception {

        FriendDTO.CreateFriendRequest createFriendRequest = new FriendDTO.CreateFriendRequest(friend1User);
        FriendDTO.FriendSimpleInfoResponse friend = friendService.createFriend(meUser, createFriendRequest);
        FriendDTO.UpdateFriendNameRequest updateFriendNameRequest = new FriendDTO.UpdateFriendNameRequest("");

        Throwable exception = assertThrows(MethodArgumentNotValidException.class, () -> {
//            friendService.updateFriendName(friend.getFriendId(), updateFriendNameRequest);
            new FriendDTO.UpdateFriendNameRequest(null);
        });

        assertEquals("친구이름은 Null 일 수 없습니다", exception.getMessage());
    }

    @Test
    @DisplayName("친구관계를 정상적으로 삭제한다.")
    public void delete_friend_with_friend_id() throws Exception {

        FriendDTO.CreateFriendRequest createFriendRequest = new FriendDTO.CreateFriendRequest(friend1User);
        FriendDTO.FriendSimpleInfoResponse friend = friendService.createFriend(meUser, createFriendRequest);

        friendService.deleteFriend(friend.getFriendId());

        assertEquals(0, friendRepository.findAllByUserId(meUser.getId()).size());
    }

    @Test
    @DisplayName("이미 삭제되거나 없는 친구관계를 삭제하려고 한다.")
    public void delete_friend_with_same_user_id() throws Exception {
        //given
        FriendDTO.CreateFriendRequest createFriendRequest = new FriendDTO.CreateFriendRequest(friend1User);
        FriendDTO.FriendSimpleInfoResponse friend = friendService.createFriend(meUser, createFriendRequest);
        friendService.deleteFriend(friend.getFriendId());

        Throwable exception = assertThrows(EntityNotFoundException.class, () -> {
            friendService.deleteFriend(friend.getFriendId());
        });

        assertEquals("존재하지 않는 친구관계 입니다.", exception.getMessage());
    }

}

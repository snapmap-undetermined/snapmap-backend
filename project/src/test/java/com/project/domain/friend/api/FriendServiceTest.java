package com.project.domain.friend.api;

import com.project.domain.friend.dto.FriendDTO;
import com.project.domain.friend.repository.FriendRepository;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class FriendServiceTest {

    private final FriendRepository friendRepository;
    private final FriendService friendService;
    private final UserRepository userRepository;

    @Autowired
    public FriendServiceTest(FriendRepository friendRepository, FriendService friendService, UserRepository userRepository) {
        this.friendService = friendService;
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }


    public void createInitUsers() {
        Users meUser = Users.builder().email("skc@aaa.com").nickname("skc").password("123").build();
        Users friendUser = Users.builder().email("jsh@aaa.com").nickname("jsh").password("123").build();
        userRepository.save(meUser);
        userRepository.save(friendUser);
    }

    @BeforeEach
    public void initUseCase() {
        createInitUsers();
    }

    @Test
    @DisplayName("올바른 요청이 들어왔을 때 친구가 정상적으로 생성된다.")
    public void create_friend_with_correct_request_success() throws Exception {

        //given
        Users user1 = userRepository.findByEmail("skc@aaa.com").orElseThrow();
        Users user2 = userRepository.findByEmail("jsh@aaa.com").orElseThrow();
        FriendDTO.CreateFriendRequest request = new FriendDTO.CreateFriendRequest(user2);

        //when
        friendService.createFriend(user1, request);

        //then
        assertEquals(1, friendRepository.findAll().size());
    }

    @Test
    @DisplayName("유저가 본인을 선택해서 friend 생성을 할 경우에 에러가 발생한다.")
    public void create_friend_has_same_user_id_fail() {

        //given
        Users user1 = userRepository.findByEmail("skc@aaa.com").orElseThrow();
        FriendDTO.CreateFriendRequest request = new FriendDTO.CreateFriendRequest(user1);

        //when
        Exception exception = assertThrows(Exception.class, ()-> friendService.createFriend(user1, request));

        //then
        String message = exception.getMessage();
        assertEquals("user와 frienduser가 동일합니다.", message);

    }

    @Test
    public void get_all_friends_without_user_id_fail() {
        //given
        //when
        //then
    }

    @Test
    public void get_all_friends_without_user_id_success() {
        //given
        //when
        //then
    }

    @Test
    public void delete_friend_without_friend_id() {
        //given
        //when
        //then
    }

    @Test
    public void delete_friend_with_same_user_id() {
        //given
        //when
        //then
    }
}

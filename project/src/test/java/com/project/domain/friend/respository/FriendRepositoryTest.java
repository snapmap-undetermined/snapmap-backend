package com.project.domain.friend.respository;

import com.project.config.TestConfig;
import com.project.domain.friend.dto.FriendDTO;
import com.project.domain.friend.entity.Friend;
import com.project.domain.friend.repository.FriendRepository;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FriendRepositoryTest {

    @Autowired
    FriendRepository friendRepository;

    @Autowired
    UserRepository userRepository;

    Users user1;
    Users user2;

    @BeforeEach()
    public void initUser() {
        user1 = Users.builder().email("skc@test.com").password("123").nickname("skc").activated(true).phoneNumber("01000000000").build();
        user2 = Users.builder().email("jsh@test.com").password("123").nickname("jsh").activated(true).phoneNumber("01011111111").build();

        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    @DisplayName("친구 추가")
    public void friendSaveTest() {

        Friend friend = Friend.builder().me(user1).mate(user2).friendName("user2").build();
        friendRepository.save(friend);

        List<Friend> friendList = friendRepository.findAll();

        assertEquals(1, friendList.size());
    }

    @Test
    @DisplayName("친구 삭제")
    public void friendDeleteTest() {

        Friend friend = Friend.builder().me(user1).mate(user2).friendName("user2").build();
        friendRepository.save(friend);

        friendRepository.delete(friend);
        List<Friend> friendList = friendRepository.findAll();

        assertEquals(0, friendList.size());
    }

    @Test
    @DisplayName("친구 리스트 조회")
    public void friendGetTest() {

        Users user3 = Users.builder().email("skc2@test.com").password("123").nickname("skc2").activated(true).phoneNumber("01011111111").build();
        userRepository.save(user3);
        Friend friend = Friend.builder().me(user1).mate(user2).friendName("user2").build();
        Friend friend2 = Friend.builder().me(user1).mate(user3).friendName("user3").build();
        friendRepository.save(friend);
        friendRepository.save(friend2);

        List<FriendDTO.FriendResponse> friendList = friendRepository.findAllFriendsOfUser(user1.getId());

        assertEquals(2, friendList.size());

    }

    @Test
    @DisplayName("친구 중복 체크")
    public void friendExistsByUserIds() {

        Friend friend = Friend.builder().me(user1).mate(user2).friendName("user2").build();
        friendRepository.save(friend);

        boolean exists = friendRepository.existsByUserIds(user1.getId(), user2.getId());

        assertTrue(exists);
    }
}
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
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    Users user1;
    Users user2;

    @BeforeEach()
    public void initUser() {
        user1 = Users.builder().email("TEST_USER1@EMAIL.COM").password("TEST_PASSWORD").nickname("TEST_USER1").activated(true).phoneNumber("01000000000").build();
        user2 = Users.builder().email("TEST_USER2@EMAIL.COM").password("TEST_PASSWORD").nickname("TEST_USER2").activated(true).phoneNumber("01011111111").build();

        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    @DisplayName("유저별 친구 리스트 조회")
    public void find_all_friends_by_user_id() {

        Users user3 = Users.builder().email("TEST_USER3@EMAIL.COM").password("TEST_PASSWORD").nickname("TEST_USER3").activated(true).phoneNumber("01011111111").build();
        userRepository.save(user3);
        Friend friend = Friend.builder().me(user1).mate(user2).friendName("USER2").build();
        Friend friend2 = Friend.builder().me(user1).mate(user3).friendName("USER3").build();
        friendRepository.save(friend);
        friendRepository.save(friend2);

        List<FriendDTO.FriendResponse> friendList = friendRepository.findAllFriendsOfUser(user1.getId());

        assertEquals(2, friendList.size());

    }

    @Test
    @DisplayName("친구 중복 체크")
    public void friend_exists_by_user_ids() {
        Friend friend = Friend.builder().me(user1).mate(user2).friendName("USER2").build();
        friendRepository.save(friend);

        boolean exists = friendRepository.existsByUserIds(user1.getId(), user2.getId());

        assertTrue(exists);
    }
}
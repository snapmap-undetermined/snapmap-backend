package com.project.domain.friend.respository;

import com.project.config.TestConfig;
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
    @DisplayName("친구관계를 생성한다.")
    public void friendSaveTest() {
        Users user1 = Users.builder().email("skc@test.com").password("123").nickname("skc").activated(true).phoneNumber("01000000000").profileImage("abc").build();
        Users user2 = Users.builder().email("jsh@test.com").password("123").nickname("jsh").activated(true).phoneNumber("01011111111").profileImage("abc").build();

        userRepository.save(user1);
        userRepository.save(user2);
        Friend friend = Friend.builder().me(user1).mate(user2).friendName("user2").build();
        friendRepository.save(friend);

        List<Friend> friendList = friendRepository.findAll();

        assertEquals(1, friendList.size());
    }
}
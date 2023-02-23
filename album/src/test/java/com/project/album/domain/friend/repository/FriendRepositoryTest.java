package com.project.album.domain.friend.repository;

import com.project.album.config.TestConfig;
import com.project.album.common.entity.Role;
import com.project.album.domain.friend.entity.Friend;
import com.project.album.domain.users.entity.Users;
import com.project.album.domain.users.repository.UserRepository;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
public class FriendRepositoryTest {

    @Autowired
    FriendRepository friendRepository;

    @Autowired
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
    @DisplayName("friend가 제대로 생성된다.")
    public void friendSaveTest() {
        Friend friend = Friend.builder().me(meUser).friend(friendUser).build();
        friendRepository.save(friend);

        List<Friend> friendList = friendRepository.findAll();
        assertEquals(1, friendList.size());
    }

    @Test
    @DisplayName("userId를 입력해서 user와 친구인 사람의들의 목록을 조회한다.")
    public void getFriendTest() {
        Friend friend = Friend.builder().me(meUser).friend(friendUser).build();
        friendRepository.save(friend);

        List<Friend> friendList = friendRepository.findByUserId(meUser.getId());

        assertEquals(1, friendList.size());
    }

    @Test
    @DisplayName("friendId로 친구관계를 끊을 수 있다.")
    public void deleteFriend() {
        Friend friend = Friend.builder().me(meUser).friend(friendUser).build();
        friendRepository.save(friend);

        friendRepository.delete(friend);
        List<Friend> friendList = friendRepository.findAll();
        assertEquals(0, friendList.size());
    }

    @Test
    @DisplayName("friendName을 user 마음대로 수정 가능하다.")
    public void updateFriendName() {
        Friend friend = Friend.builder().me(meUser).friend(friendUser).build();
        friendRepository.save(friend);

        friend.setFriendName("testFriendName");

        Friend updatedNameFriend = friendRepository.findById(friend.getId()).orElseThrow();
        assertEquals("testFriendName", updatedNameFriend.getFriendName());
    }
}

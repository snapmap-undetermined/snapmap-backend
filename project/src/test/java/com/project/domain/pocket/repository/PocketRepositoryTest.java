package com.project.domain.pocket.repository;

import com.project.config.TestConfig;
import com.project.domain.pocket.entity.Pocket;
import com.project.domain.userpocket.entity.UserPocket;
import com.project.domain.userpocket.repository.UserPocketRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PocketRepositoryTest {

    @Autowired
    private PocketRepository pocketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPocketRepository userPocketRepository;

    Users testUser;

    @BeforeEach
    void initData() {
        testUser = Users.builder().email("TEST@EMAIL.COM").password("TEST_PASSWORD").nickname("TEST_NICKNAME").activated(true).phoneNumber("01000000000").build();
        userRepository.save(testUser);
    }

    @Test
    @DisplayName("특정 유저가 속한 포켓 리스트 조회시, 초대를 수락하지 않은 포켓은 제외 시킨다.")
    public void find_all_pocket_by_user_id_when_not_accept_invitation() {

        Pocket pocket = Pocket.builder().master(testUser).pocketKey("POCKET1_KEY").description("TEST_POCKET1_DESC").name("TEST_POCKET1").imageUrl("IMAGE_URL").build();
        Pocket pocket2 = Pocket.builder().master(testUser).pocketKey("POCKET2_KEY").description("TEST_POCKET2_DESC").name("TEST_POCKET2").imageUrl("IMAGE_URL").build();
        pocketRepository.save(pocket);
        pocketRepository.save(pocket2);
        userPocketRepository.save(UserPocket.builder().pocket(pocket).user(testUser).activated(true).build());
        userPocketRepository.save(UserPocket.builder().pocket(pocket2).user(testUser).activated(false).build());

        List<Pocket> pocketList = pocketRepository.findAllPocketByUserId(testUser.getId());

        assertEquals(1, pocketList.size());
    }

    @Test
    @DisplayName("특정 유저가 속한 포켓 리스트를 조회 한다.")
    public void find_all_pocket_by_user_id() {

        Pocket pocket = Pocket.builder().master(testUser).pocketKey("POCKET1_KEY").description("TEST_POCKET1_DESC").name("TEST_POCKET1").imageUrl("IMAGE_URL").build();
        Pocket pocket2 = Pocket.builder().master(testUser).pocketKey("POCKET2_KEY").description("TEST_POCKET2_DESC").name("TEST_POCKET2").imageUrl("IMAGE_URL").build();
        pocketRepository.save(pocket);
        pocketRepository.save(pocket2);
        userPocketRepository.save(UserPocket.builder().pocket(pocket).user(testUser).activated(true).build());
        userPocketRepository.save(UserPocket.builder().pocket(pocket2).user(testUser).activated(true).build());

        List<Pocket> pocketList = pocketRepository.findAllPocketByUserId(testUser.getId());

        // pocketList 안에 있는 Pocket 객체의 name 값과 pocket, pocket2의 name 값을 비교한다.
        boolean isFound = pocketList.stream()
                .anyMatch(p -> p.getName().equals(pocket.getName()) || p.getName().equals(pocket2.getName()));
        // name 값이 일치하는 경우에는 성공적으로 테스트가 통과되었으므로 성공처리 한다.
        assertTrue(isFound);
    }

    @Test
    @DisplayName("포켓 키를 이용해서 포켓 정보를 조회한다.")
    public void find_pocket_by_pocket_key() {

        pocketRepository.save(Pocket.builder().master(testUser).pocketKey("POCKET_KEY").description("TEST_POCKET_DESC").name("TEST_POCKET").imageUrl("IMAGE_URL").build());

        Pocket pocket = pocketRepository.findPocketByKey("POCKET_KEY");

        assertEquals(pocket.getName(), "TEST_POCKET");
    }

}

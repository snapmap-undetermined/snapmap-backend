package com.project.domain.pin.repository;

import com.project.config.TestConfig;
import com.project.domain.pin.entity.Pin;
import com.project.domain.pocket.entity.Pocket;
import com.project.domain.pocket.repository.PocketRepository;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PinRepositoryTest {

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PocketRepository pocketRepository;

    Users testUser;

    @BeforeEach
    void initData() {
        testUser = Users.builder().email("TEST@EMAIL.COM").password("TEST_PASSWORD").nickname("TEST_NICKNAME").activated(true).phoneNumber("01000000000").build();
        userRepository.save(testUser);
    }

    @Test
    @DisplayName("포켓에 들어있는 모든 핀들을 조회 한다.")
    public void find_all_pin_by_pocket_id() {

        Pocket pocket = Pocket.builder().master(testUser).pocketKey("POCKET1_KEY").description("TEST_POCKET1_DESC").name("TEST_POCKET1").imageUrl("IMAGE_URL").build();
        pocketRepository.save(pocket);
        Pin testPin1 = createTestPin(testUser, pocket);
        Pin testPin2 = createTestPin(testUser, pocket);
        Pin testPin3 = createTestPin(testUser, pocket);

        Pageable pageable = PageRequest.of(0, 3);
        Page<Pin> allPins = pinRepository.findAllByPocketId(pocket.getId(), pageable);
        List<Pin> pinList = allPins.getContent();

        assertEquals(3, pinList.size());
    }

    private Pin createTestPin(Users testUser, Pocket pocket) {
        return pinRepository.save(Pin.builder().user(testUser).pocket(pocket).build());
    }
}

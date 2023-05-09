package com.project.domain.comment.repository;

import com.project.config.TestConfig;
import com.project.domain.comment.entity.PinComment;
import com.project.domain.pin.entity.Pin;
import com.project.domain.pin.repository.PinRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepositoryTest {

    @Autowired
    private PinCommentRepository pinCommentRepository;

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PocketRepository pocketRepository;

    Users testUser;

    Pin testPin;

    @BeforeEach
    public void initData() {
        testUser = Users.builder().email("TEST@EMAIL.COM").password("TEST_PASSWORD").nickname("TEST_NICKNAME").activated(true).phoneNumber("01000000000").build();
        userRepository.save(testUser);
        Pocket testPocket = Pocket.builder().master(testUser).pocketKey("POCKET1_KEY").description("TEST_POCKET1_DESC").name("TEST_POCKET1").imageUrl("IMAGE_URL").build();
        pocketRepository.save(testPocket);
        testPin = Pin.builder().user(testUser).pocket(testPocket).build();
        pinRepository.save(testPin);
    }

    @Test
    @DisplayName("댓글 순서 번호로 댓글을 조회 한다.")
    public void find_comment_by_comment_order() {

        PinComment testPinComment = PinComment.builder().commentOrder(1L).text("TEST_COMMENT_TEXT").pin(testPin).writer(testUser).isDeleted(false).build();
        pinCommentRepository.save(testPinComment);

        PinComment pinComment = pinCommentRepository.findByCommentOrder(1L);

        assertEquals("TEST_COMMENT_TEXT", pinComment.getText());
    }

    @Test
    @DisplayName("해당 핀이 가지고 있는 가장 마지막 댓글 순서 번호를 조회 한다.")
    public void get_last_pin_comment_order() {

        createTestPinComment(testPin,testUser, 1L);
        createTestPinComment(testPin,testUser, 2L);

        Long lastPinCommentOrder = pinCommentRepository.getLastPinCommentOrder(testPin.getId());

        assertEquals(2L,lastPinCommentOrder);

    }

    private void createTestPinComment(Pin testPin, Users testUser, Long commentOrder) {
        pinCommentRepository.save(PinComment.builder().commentOrder(commentOrder).text("TEST_COMMENT_TEXT").pin(testPin).writer(testUser).isDeleted(false).build());
    }

}

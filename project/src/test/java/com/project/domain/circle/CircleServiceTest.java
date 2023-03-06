package com.project.domain.circle;

import com.project.domain.circle.api.CircleService;
import com.project.domain.circle.dto.CircleDTO;
import com.project.domain.circle.entity.Circle;
import com.project.domain.circle.repository.CircleRepository;
import com.project.domain.usercircle.repository.UserCircleRepository;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class CircleServiceTest {

    private final CircleService circleService;
    private final CircleRepository circleRepository;
    private final UserRepository userRepository;
    private final UserCircleRepository userCircleRepository;

    Users user1;

    @Autowired
    public CircleServiceTest(CircleService circleService, CircleRepository circleRepository, UserRepository userRepository, UserCircleRepository userCircleRepository) {
        this.circleService = circleService;
        this.circleRepository = circleRepository;
        this.userRepository = userRepository;
        this.userCircleRepository = userCircleRepository;
    }


    public void createInitUsers() {
        user1 = Users.builder().email("skc@aaa.com").nickname("skc").password("123").build();
        userRepository.save(user1);
    }

    @BeforeEach
    public void initUseCase() {
        createInitUsers();
    }

    @Test
    @DisplayName("요청 값이 올바를 경우 그룹이 생성된다.")
    public void create_circle_with_valid_request() {

        CircleDTO.CreateCircleRequest request = new CircleDTO.CreateCircleRequest("testCircleName");

        CircleDTO.CircleSimpleInfoResponse response = circleService.createCircle(user1, request);

        assertEquals("testCircleName", response.getCircleName());
    }

    @Test
    @DisplayName("요청 값이 올바를 경우 유저-그룹 관계도 생성된다.")
    public void create_user_circle_with_valid_request() {

        CircleDTO.CreateCircleRequest request = new CircleDTO.CreateCircleRequest("testCircleName");
        CircleDTO.CircleSimpleInfoResponse circle = circleService.createCircle(user1, request);

        List<Circle> userCircleList = userCircleRepository.findAllCircleByUserId(user1.getId());

        assertEquals(circle.getCircleId(), userCircleList.get(0).getId());
    }

    @Test
    @DisplayName("유저의 정보로 유저가 속한 모든 그룹을 조회 할 수 있다.")
    public void get_circle_list_has_user_id_success() throws Exception {
        CircleDTO.CreateCircleRequest request = new CircleDTO.CreateCircleRequest("testCircleName");
        CircleDTO.CircleSimpleInfoResponse circle = circleService.createCircle(user1, request);

//        List<CircleDTO.CircleSimpleInfoResponse>
    }
//    @Test
//    @DisplayName("유저의 정보로 유저가 속한 모든 그룹을 조회 할 수 있다.")
//    public void get_circle_list_has_user_id_success() throws Exception {
//
//        Users user2 = Users.builder().email("jsh@aaa.com").nickname("jsh").password("123").build();
//        userRepository.save(user2);
//        CircleDTO.CreateCircleRequest request = new CircleDTO.CreateCircleRequest("testCircleName");
//        CircleDTO.CircleSimpleInfoResponse circle = circleService.createCircle(user1, request);
//        CircleDTO.JoinCircleRequest joinCircleRequest = new CircleDTO.JoinCircleRequest(circle.getCircleId());
//        circleService.joinCircle(user2, joinCircleRequest);
//
//        CircleDTO.CircleWithJoinUserResponse joinUserResponse = circleService.getUserListByCircle(circle.getCircleId());
//
//        assertEquals(2,joinUserResponse.getJoinedUserList().size());
//    }

}

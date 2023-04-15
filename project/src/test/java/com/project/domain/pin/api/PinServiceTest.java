package com.project.domain.pin.api;

import com.project.common.exception.BusinessLogicException;
import com.project.domain.pocket.entity.Pocket;
import com.project.domain.pocket.repository.PocketRepository;
import com.project.domain.location.dto.LocationDTO;
import com.project.domain.location.dto.PointDTO;
import com.project.domain.pin.dto.PinDTO;
import com.project.domain.pin.repository.PinRepository;
import com.project.domain.userpocket.entity.UserPocket;
import com.project.domain.userpocket.repository.UserPocketRepository;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
@Transactional
class PinServiceTest {

    private final UserRepository userRepository;
    private final PocketRepository pocketRepository;
    private final UserPocketRepository userPocketRepository;
    private final PinService pinService;
    private final PinRepository pinRepository;

    @Autowired
    public PinServiceTest(UserRepository userRepository, PocketRepository pocketRepository, UserPocketRepository userPocketRepository, PinServiceImpl pinService, PinRepository pinRepository) {
        this.userRepository = userRepository;
        this.pocketRepository = pocketRepository;
        this.userPocketRepository = userPocketRepository;
        this.pinService = pinService;
        this.pinRepository = pinRepository;
    }

    private MockMultipartFile generateFile(String name, String originalFileName) {
        return new MockMultipartFile(name, originalFileName, "image/png", "<<png data>>".getBytes());
    }

    private Users generateSimpleUser(String email, String nickname) {
        Users user = Users.builder().email(email).nickname(nickname).build();
        return userRepository.save(user);
    }

    private Pocket generateSimplePocket(String name) {
        Pocket pocket = Pocket.builder().name(name).build();
        return pocketRepository.save(pocket);
    }


    private void userJoinCircle(Users user, Pocket pocket) {
        pocketRepository.save(pocket);
        userPocketRepository.save(UserPocket.builder().user(user).pocket(pocket).build());
    }

    private List<String> generateTagNames(String... tagNames) {
        return List.of(tagNames);
    }

    @Test
    @DisplayName("모든 파라미터가 유효할 때 핀이 생성된다.")
    public void create_pin_with_valid_param_success() {
        // Given
        Users user = generateSimpleUser("test-email", "test-nickname");
        Pocket pocket = generateSimplePocket("test-circle");
        userJoinCircle(user, pocket);
        LocationDTO locationDTO = new LocationDTO("location", new PointDTO(123.123, 123.456));
        List<String> tagNames = generateTagNames("tag1", "tag2");
        PinDTO.PinCreateRequest request = PinDTO.PinCreateRequest.builder().location(locationDTO).tagNames(tagNames).build();
        List<MultipartFile> pictures = List.of(generateFile("p1", "p1"),generateFile("p2", "p2"));

        // When
        PinDTO.PinDetailResponse createdPin = pinService.createPin(user, pocket.getId(), request, pictures);
        log.info("createdPin = " + createdPin);

        // Then
        Assertions.assertNotNull(pinRepository.findById(createdPin.getId()));
    }

    @Test
    @DisplayName("사진이 존재하지 않을 때 핀을 생성할 수 없다")
    public void create_pin_with_no_picture_not_allowed() {
        // Given
        Users user = generateSimpleUser("test-email", "test-nickname");
        Pocket pocket = generateSimplePocket("test-circle");
        userJoinCircle(user, pocket);
        LocationDTO locationDTO = new LocationDTO("location", new PointDTO(123.123, 123.456));
        PinDTO.PinCreateRequest request = PinDTO.PinCreateRequest.builder().location(locationDTO).build();
        List<MultipartFile> pictures = new ArrayList<>();

        // When
        Throwable exception = Assertions.assertThrows(BusinessLogicException.class, () -> {
            pinService.createPin(user, pocket.getId(), request, pictures);
        });

        // Then
        Assertions.assertEquals("핀에 저장할 사진이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("자신 혹은 자신이 가입한 써클의 핀을 조회할 수 있다.")
    public void get_pin_detail_by_me_or_circle_success() {
        // Given
        Users user = generateSimpleUser("test-email", "test-nickname");
        Pocket pocket = generateSimplePocket("test-circle");
        userJoinCircle(user, pocket);

        LocationDTO locationDTO = new LocationDTO("location", new PointDTO(123.123, 123.456));
        List<MultipartFile> pictures = List.of(generateFile("p1", "p1"),generateFile("p2", "p2"));
        PinDTO.PinCreateRequest request = PinDTO.PinCreateRequest.builder().location(locationDTO).build();
        PinDTO.PinDetailResponse createdPin = pinService.createPin(user, pocket.getId(), request, pictures);

        // When
        PinDTO.PinDetailResponse pinDetail = pinService.getPinDetail(user, createdPin.getId());
        log.info("getPinDetail = " + pinDetail);

        // Then
        Assertions.assertEquals("location", pinDetail.getLocation().getName());

    }

    @Test
    @DisplayName("자신이 생성한 모든 핀을 조회할 수 있다.")
    public void get_all_pins_detail_by_me_success() {
        // Given
        Users user = generateSimpleUser("test-email", "test-nickname");
        Pocket pocket = generateSimplePocket("test-circle");
        userJoinCircle(user, pocket);

        LocationDTO locationDTO = new LocationDTO("location", new PointDTO(123.123, 123.456));
        PinDTO.PinCreateRequest request1 = PinDTO.PinCreateRequest.builder().location(locationDTO).build();
        PinDTO.PinCreateRequest request2 = PinDTO.PinCreateRequest.builder().location(locationDTO).build();
        List<MultipartFile> pictures1 = List.of(generateFile("p1", "p1"),generateFile("p2", "p2"));
        List<MultipartFile> pictures2 = List.of(generateFile("p3", "p3"),generateFile("p4", "p4"));

        pinService.createPin(user, pocket.getId(), request1, pictures1);
        pinService.createPin(user, pocket.getId(), request2, pictures2);

        // When
        PinDTO.PinDetailListResponse allPinByMe = pinService.getAllPinByMe(user);

        // Then
        Assertions.assertEquals(2, allPinByMe.getPinDetailResponseList().size());
    }

    @Test
    @DisplayName("존재하지 않는 핀을 조회할 수 없다.")
    public void get_not_exist_pin_throws_exception() {
        // Given
        Users user = generateSimpleUser("test-email", "test-nickname");

        // When
        Throwable exception = Assertions.assertThrows(BusinessLogicException.class, () -> {
            pinService.getPinDetail(user, -1L);
        });

        // Then
        Assertions.assertEquals("존재하지 않는 핀입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("내가 포함된 써클이 아닌 써클의 핀을 조회할 수 없다.")
    public void get_pin_from_external_circle_not_allowed() {
        // Given
        Users me = generateSimpleUser("test-email1", "test-nickname1");
        Users stranger = generateSimpleUser("test-email2", "test-nickname2");
        Pocket pocket = generateSimplePocket("test-circle");
        userJoinCircle(stranger, pocket);

        LocationDTO locationDTO = new LocationDTO("location", new PointDTO(123.123, 123.456));
        List<MultipartFile> pictures = List.of(generateFile("p1", "p1"),generateFile("p2", "p2"));
        PinDTO.PinCreateRequest request = PinDTO.PinCreateRequest.builder().location(locationDTO).build();
        PinDTO.PinDetailResponse createdPin = pinService.createPin(stranger, pocket.getId(), request, pictures);

        // When
        Throwable exception = Assertions.assertThrows(BusinessLogicException.class, () -> {
            pinService.getPinDetail(me, createdPin.getId());
        });

        // Then
        Assertions.assertEquals("해당 핀에 대한 접근 권한이 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("가입한 써클 내 모든 핀을 조회할 수 있다.")
    public void get_all_pins_by_inner_circle_success() {
        // Given
        Users me = generateSimpleUser("test-email1", "test-nickname1");
        Users friend = generateSimpleUser("test-email2", "test-nickname2");
        Pocket pocket = generateSimplePocket("test-circle");
        userJoinCircle(me, pocket);
        userJoinCircle(friend, pocket);

        LocationDTO locationDTO = new LocationDTO("location", new PointDTO(123.123, 123.456));
        PinDTO.PinCreateRequest request1 = PinDTO.PinCreateRequest.builder().location(locationDTO).build();
        PinDTO.PinCreateRequest request2 = PinDTO.PinCreateRequest.builder().location(locationDTO).build();
        List<MultipartFile> pictures1 = List.of(generateFile("p1", "p1"),generateFile("p2", "p2"));
        List<MultipartFile> pictures2 = List.of(generateFile("p3", "p3"),generateFile("p4", "p4"));

        pinService.createPin(friend, pocket.getId(), request1, pictures1);
        pinService.createPin(friend, pocket.getId(), request2, pictures2);

        // When
        PinDTO.PinDetailListResponse allPins = pinService.getAllPinsByPocket(pocket.getId());

        // Then
        Assertions.assertEquals(2, allPins.getPinDetailResponseList().size());

    }

    @Test
    @DisplayName("핀의 위치를 수정할 수 있다.")
    public void update_pin_location_success() throws ParseException {
        // Given
        Users me = generateSimpleUser("test-email1", "test-nickname1");
        Pocket pocket = generateSimplePocket("test-circle");
        userJoinCircle(me, pocket);

        LocationDTO locationDTOBefore = new LocationDTO("before", new PointDTO(123.123, 123.456));
        PinDTO.PinCreateRequest request = PinDTO.PinCreateRequest.builder().location(locationDTOBefore).build();
        List<MultipartFile> pictures = List.of(generateFile("p1", "p1"),generateFile("p2", "p2"));
        PinDTO.PinDetailResponse createdPin = pinService.createPin(me, pocket.getId(), request, pictures);

        LocationDTO locationDTOAfter = new LocationDTO("after", new PointDTO(123.123, 123.456));
        PinDTO.PinUpdateRequest updateRequest = PinDTO.PinUpdateRequest.builder().location(locationDTOAfter).build();

        // When
        PinDTO.PinDetailResponse updatedPin = pinService.updatePin(me, createdPin.getId(), updateRequest, pictures);

        // Then
        Assertions.assertEquals("after", updatedPin.getLocation().getName());
    }

    @Test
    @DisplayName("핀의 사진 목록을 수정할 수 있다.")
    public void update_pin_pictures_success() throws ParseException {
        // Given
        Users me = generateSimpleUser("test-email1", "test-nickname1");
        Pocket pocket = generateSimplePocket("test-circle");
        userJoinCircle(me, pocket);

        LocationDTO locationDTO = new LocationDTO("location", new PointDTO(123.123, 123.456));
        PinDTO.PinCreateRequest request = PinDTO.PinCreateRequest.builder().location(locationDTO).build();
        List<MultipartFile> picturesBefore = List.of(generateFile("p1", "p1"),generateFile("p2", "p2"));

        PinDTO.PinDetailResponse createdPin = pinService.createPin(me, pocket.getId(), request, picturesBefore);
        List<MultipartFile> picturesAfter = List.of(generateFile("p3", "p3"));

        // When
        PinDTO.PinDetailResponse updatedPin = pinService.updatePin(me, createdPin.getId(), null, picturesAfter);

        // Then
        Assertions.assertEquals(1, updatedPin.getPictureList().size());
        Assertions.assertEquals("p3", updatedPin.getPictureList().get(0).getOriginalName());
    }

    @Test
    @DisplayName("자신이 만든 핀을 삭제할 수 있다.")
    public void delete_pin_created_by_me_success() {
        // Given
        Users me = generateSimpleUser("test-email1", "test-nickname1");
        Pocket pocket = generateSimplePocket("test-circle");
        userJoinCircle(me, pocket);

        LocationDTO locationDTO = new LocationDTO("location", new PointDTO(123.123, 123.456));
        PinDTO.PinCreateRequest request = PinDTO.PinCreateRequest.builder().location(locationDTO).build();
        List<MultipartFile> pictures = List.of(generateFile("p1", "p1"),generateFile("p2", "p2"));
        PinDTO.PinDetailResponse createdPin = pinService.createPin(me, pocket.getId(), request, pictures);

        // When
        pinService.deletePin(me, createdPin.getId());

        // Then
        Assertions.assertEquals(0, pinRepository.findByUserId(me.getId()).size());
    }

    @Test
    @DisplayName("존재하지 않는 핀을 삭제할 수 없다.")
    public void delete_not_exist_pin_not_allowed() {
        // Given
        Users me = generateSimpleUser("test-email1", "test-nickname1");

        // When
        Throwable exception = Assertions.assertThrows(BusinessLogicException.class, () -> {
            pinService.deletePin(me, -1L);
        });

        // Then
        Assertions.assertEquals("존재하지 않는 핀입니다.", exception.getMessage());

    }

    @Test
    @DisplayName("자신이 생성하지 않은 핀은 삭제할 수 없다.")
    public void delete_pin_not_created_by_me_not_allowed() {
        // Given
        Users me = generateSimpleUser("test-email1", "test-nickname1");
        Users stranger = generateSimpleUser("test-email2", "test-nickname2");
        Pocket pocket = generateSimplePocket("test-circle");
        userJoinCircle(stranger, pocket);

        LocationDTO locationDTO = new LocationDTO("location", new PointDTO(123.123, 123.456));
        List<MultipartFile> pictures = List.of(generateFile("p1", "p1"),generateFile("p2", "p2"));
        PinDTO.PinCreateRequest request = PinDTO.PinCreateRequest.builder().location(locationDTO).build();
        PinDTO.PinDetailResponse createdPin = pinService.createPin(stranger, pocket.getId(), request, pictures);

        // When
        Throwable exception = Assertions.assertThrows(BusinessLogicException.class, () -> {
            pinService.deletePin(me, createdPin.getId());
        });

        // Then
        Assertions.assertEquals("해당 핀에 대한 접근 권한이 없습니다.", exception.getMessage());
    }
}
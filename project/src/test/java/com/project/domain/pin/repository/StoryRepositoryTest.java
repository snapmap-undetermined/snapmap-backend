package com.project.domain.pin.repository;

import com.project.config.TestConfig;
import com.project.domain.circle.entity.Circle;
import com.project.domain.circle.repository.CircleRepository;
import com.project.domain.circlepin.entity.CirclePin;
import com.project.domain.circlepin.repository.CirclePinRepository;
import com.project.domain.picture.entity.Picture;
import com.project.domain.pin.dto.PinDTO;
import com.project.domain.pin.entity.Pin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
class PinRepositoryTest {

    @Autowired
    private PinRepository pinRepository;

    @Autowired
    private CircleRepository circleRepository;

    @Autowired
    private CirclePinRepository circlePinRepository;

    private final Picture pictureA = Picture.builder().originalName("A").url("urlA").build();
    private final Picture pictureB = Picture.builder().originalName("B").url("urlB").build();


    @Test
    @DisplayName("핀 저장 테스트")
    void pin_save_test() throws ParseException {

        List<Pin> pins = Arrays.asList(
                Pin.builder()
                        .location(toPoint(new PinDTO.PointDTO(123.123, 123.123)))
                        .pictureList(Collections.singletonList(pictureA))
                        .build()
                , Pin.builder()
                        .location(toPoint(new PinDTO.PointDTO(123.123, 123.123)))
                        .pictureList(Collections.singletonList(pictureB))
                        .build()
        );List<Pin> savedPinList = pinRepository.saveAll(pins);
        assertEquals(2, savedPinList.size());
    }

    @Test
    @DisplayName("핀 조회 테스트 By pinId")
    void pin_findAll_test() throws ParseException {
        Pin pin = Pin.builder()
                .location(toPoint(new PinDTO.PointDTO(123.123, 123.123)))
                .pictureList(Collections.singletonList(pictureA))
                .build();

        Long id = pinRepository.save(pin).getId();

        Optional<Pin> result = pinRepository.findById(id);
        assertTrue(result.isPresent());
        assertEquals(result.get(), pin);
    }

    @Test
    @DisplayName("핀 조회 테스트 By CircleId")
    void pin_findByCircleId_test() throws ParseException {

        Pin pin = Pin.builder()
                .location(toPoint(new PinDTO.PointDTO(1.1, 2.2)))
                .pictureList(Collections.singletonList(pictureA))
                .build();
        Circle circle = Circle.builder().name("circleA").build();
        pinRepository.save(pin);
        Long circleId = circleRepository.save(circle).getId();
        circlePinRepository.save(CirclePin.builder().pin(pin).circle(circle).build());

        List<Pin> result = pinRepository.findAllByCircleId(circleId);
        System.out.println("result = " + result);

        assertEquals("A", result.get(0).getPictureList().get(0).getOriginalName());
    }

    public static Point toPoint(PinDTO.PointDTO pointDTO) throws ParseException {
        final String pointWKT = String.format("POINT(%s %s)", pointDTO.getLongitude(), pointDTO.getLongitude());
        return (Point) new WKTReader().read(pointWKT);
    }
}
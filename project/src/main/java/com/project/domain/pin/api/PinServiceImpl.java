package com.project.domain.pin.api;

import com.project.common.exception.BusinessLogicException;
import com.project.common.exception.EntityNotFoundException;
import com.project.common.exception.ErrorCode;
import com.project.common.handler.S3Uploader;
import com.project.domain.circle.entity.Circle;
import com.project.domain.circle.repository.CircleRepository;
import com.project.domain.location.entity.Location;
import com.project.domain.location.repository.LocationRepository;
import com.project.domain.picture.dto.PictureDTO;
import com.project.domain.picture.entity.Picture;
import com.project.domain.picture.repository.PictureRepository;
import com.project.domain.pin.dto.PinDTO;
import com.project.domain.pin.entity.Pin;
import com.project.domain.pin.repository.PinRepository;
import com.project.domain.pintag.entity.PinTag;
import com.project.domain.tag.entity.Tag;
import com.project.domain.tag.repository.TagRepository;
import com.project.domain.usercircle.repository.UserCircleRepository;
import com.project.domain.users.entity.Users;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.io.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PinServiceImpl implements PinService {

    private final PinRepository pinRepository;
    private final CircleRepository circleRepository;
    private final UserCircleRepository userCircleRepository;
    private final S3Uploader s3Uploader;
    private final LocationRepository locationRepository;
    private final TagRepository tagRepository;
    private final PictureRepository pictureRepository;


    @Override
    @Transactional
    public PinDTO.PinDetailResponse createPin(Users user, Long circleId, PinDTO.PinCreateRequest request, List<MultipartFile> pictures) {
        Circle circle = getCircle(circleId);
        validatePictureInput(pictures);
        validateUserMembershipOnCircle(user, circle);

        Pin pin = request.toEntity();
        user.addPin(pin); // 유저에 핀 추가
        circle.addPin(pin); // 그룹에 핀 추가

        locationRepository.save(pin.getLocation());

        for (String tagName : request.getTagNames()) {
            Tag tag = tagRepository.findByName(tagName).orElseGet(() -> {
                Tag newTag = Tag.builder().name(tagName).build();
                return tagRepository.save(newTag);
            });
            PinTag pinTag = PinTag.builder().pin(pin).tag(tag).build();
            pin.addPinTag(pinTag);
        }

        List<Picture> pictureList = s3Uploader.uploadAndSavePictures(pictures);
        pictureList.forEach(pin::addPicture);
        pinRepository.save(pin);
        return new PinDTO.PinDetailResponse(pin);
    }

    @Override
    public PinDTO.PinDetailResponse getPinDetail(Users user, Long pinId) {
        Pin pin = getPin(pinId);

        List<Circle> userJoinCircles = circleRepository.findAllCircleByUserId(user.getId());
        checkPinAccessibility(user, userJoinCircles, pin);

        return new PinDTO.PinDetailResponse(pin);
    }

    @Override
    public PinDTO.PinDetailListResponse getAllPinsByCircle(Long circleId) {
        // Pin을 모두 조회하고, 각 Pin에 존재하는 사진을 가져온다.
        List<Pin> allPins = pinRepository.findAllByCircleId(circleId);
        List<PinDTO.PinDetailResponse> pinDetailResponseList = allPins.stream().map(PinDTO.PinDetailResponse::new).toList();
        return new PinDTO.PinDetailListResponse(pinDetailResponseList);
    }

    @Override
    public PinDTO.PinDetailListResponse getAllPinByMe(Users user) {
        List<Pin> allPins = pinRepository.findByUserId(user.getId());
        List<PinDTO.PinDetailResponse> pinDetailResponseList = allPins.stream().map(PinDTO.PinDetailResponse::new).toList();
        return new PinDTO.PinDetailListResponse(pinDetailResponseList);
    }

    @Override
    @Transactional
    public PinDTO.PinDetailResponse updatePin(Users user, Long pinId, PinDTO.PinUpdateRequest request, List<MultipartFile> pictures) throws ParseException {
        Pin pin = getPin(pinId);

        // Title, Location 정보의 변화가 있는가?
        if (request != null) {
            Location updatedLocation = request.getLocation().toEntity();
            locationRepository.save(updatedLocation);
            pin.setLocation(updatedLocation);
        }

        // 사진 수정. 새로운 사진 목록에 최소 한 장 이상의 사진이 존재해야 한다.
        validatePictureInput(pictures);

        List<Picture> pictureList = s3Uploader.uploadAndSavePictures(pictures);
        pin.getPictures().clear();
        pictureList.forEach(pin::addPicture);

        return new PinDTO.PinDetailResponse(pin);
    }

    @Override
    @Transactional
    public void deletePin(Users user, Long pinId) {
        Pin pin = getPin(pinId);
        if (isPinCreatedByUser(user, pin)) {
            pin.getCircle().removePin(pin); // 써클에서 해당 핀 삭제
            user.removePin(pin); // 유저에서 해당 핀 삭제
            pin.setActivated(pin.getActivated());
        } else {
            throw new BusinessLogicException("해당 핀에 대한 접근 권한이 없습니다.", ErrorCode.HANDLE_ACCESS_DENIED);
        }
    }

    @Override
    public PictureDTO.PictureResponse getPictureDetail(Users user, Long pictureId) {

        Picture picture = pictureRepository.findById(pictureId).orElseThrow(() -> {
            throw new EntityNotFoundException("존재하지 않는 그림 입니다.");
        });

        return new PictureDTO.PictureResponse(picture);
    }

    private boolean isPinCreatedByUser(Users user, Pin pin) {
        List<Pin> myPins = pinRepository.findByUserId(user.getId());
        return myPins.contains(pin);
    }

    private boolean isPinCreatedByCircle(List<Circle> userJoinCircles, Pin pin) {
        for (Circle circle : userJoinCircles) {
            List<Pin> pinsByCircle = pinRepository.findAllByCircleId(circle.getId());
            if (pinsByCircle.contains(pin)) {
                return true;
            }
        }
        return false;
    }

    private void checkPinAccessibility(Users user, List<Circle> userJoinCircles, Pin pin) {
        if (!isPinCreatedByUser(user, pin) && !isPinCreatedByCircle(userJoinCircles, pin)) {
            throw new BusinessLogicException("해당 핀에 대한 접근 권한이 없습니다.", ErrorCode.HANDLE_ACCESS_DENIED);
        }
    }

    private void validatePictureInput(List<MultipartFile> pictures) {
        if (pictures == null || pictures.isEmpty()) {
            throw new BusinessLogicException("핀에 저장할 사진이 존재하지 않습니다.",ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    private void validateUserMembershipOnCircle(Users user, Circle circle) {
        if(userCircleRepository.findByUserIdAndCircleId(user.getId(), circle.getId()).isEmpty()){
            throw new EntityNotFoundException("가입하지 않은 써클입니다.");
        }
    }

    private Circle getCircle(Long circleId) {
        return circleRepository.findById(circleId).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 써클입니다."));
    }

    private Pin getPin(Long pinId) {
        return pinRepository.findById(pinId).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 핀입니다."));
    }
}

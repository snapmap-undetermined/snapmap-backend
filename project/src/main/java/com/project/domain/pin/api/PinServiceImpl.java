package com.project.domain.pin.api;

import com.project.common.exception.BusinessLogicException;
import com.project.common.exception.EntityNotFoundException;
import com.project.common.exception.ErrorCode;
import com.project.common.handler.S3Uploader;
import com.project.domain.circle.entity.Circle;
import com.project.domain.circle.repository.CircleRepository;
import com.project.domain.circlepin.entity.CirclePin;
import com.project.domain.circlepin.repository.CirclePinRepository;
import com.project.domain.location.entity.Location;
import com.project.domain.location.repository.LocationRepository;
import com.project.domain.picture.entity.Picture;
import com.project.domain.picture.repository.PictureRepository;
import com.project.domain.pin.dto.PinDTO;
import com.project.domain.pin.entity.Pin;
import com.project.domain.pin.repository.PinRepository;
import com.project.domain.pinpicture.entity.PinPicture;
import com.project.domain.pinpicture.repository.PinPictureRepository;
import com.project.domain.pintag.entity.PinTag;
import com.project.domain.pintag.repository.PinTagRepository;
import com.project.domain.tag.entity.Tag;
import com.project.domain.tag.repository.TagRepository;
import com.project.domain.usercircle.repository.UserCircleRepository;
import com.project.domain.users.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.io.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PinServiceImpl implements PinService {

    private final PinRepository pinRepository;
    private final PictureRepository pictureRepository;
    private final CircleRepository circleRepository;
    private final UserCircleRepository userCircleRepository;
    private final CirclePinRepository circlePinRepository;
    private final PinPictureRepository pinPictureRepository;
    private final S3Uploader s3Uploader;
    private final LocationRepository locationRepository;
    private final PinTagRepository pinTagRepository;

    private final TagRepository tagRepository;


    @Override
    public PinDTO.PinDetailResponse createPin(Users user, Long circleId, PinDTO.PinCreateRequest request, List<MultipartFile> pictures) {
        Pin pin = request.toEntity();
        Circle circle = getCircle(circleId);

        validateUserMembershipOnCircle(user, circle);
        validatePictureInput(pictures);
        // 양방향 연관관계 매핑
        user.addPin(pin);

        locationRepository.save(pin.getLocation());
        circlePinRepository.save(CirclePin.builder().circle(circle).pin(pin).build());

        List<PinTag> pinTags = new ArrayList<>();
        for (String tagName : request.getTagNames()) {
            Tag tag = tagRepository.findByName(tagName).orElseGet(() -> {
                Tag newTag = Tag.builder().name(tagName).build();
                return tagRepository.save(newTag);
            });
            PinTag pinTag = PinTag.builder().pin(pin).tag(tag).build();
            pinTags.add(pinTag);
        }
        pin.setPinTags(pinTags);

        List<Picture> pictureList = uploadAndSavePictures(pictures);
        List<PinPicture> pinPictures = pictureList.stream()
                .map((p) -> PinPicture.builder().pin(pin).picture(p).build())
                .collect(Collectors.toList());
        pin.setPinPictures(pinPictures);

        pinRepository.save(pin);
        return new PinDTO.PinDetailResponse(pin);
    }



    @Override
    public PinDTO.PinDetailResponse getPinDetail(Users user, Long pinId) {
        Pin pin = getPin(pinId);

        List<Circle> userJoinCircles = userCircleRepository.findAllCircleByUserId(user.getId());

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
    public PinDTO.PinDetailResponse updatePin(Users user, Long pinId, PinDTO.PinUpdateRequest request, List<MultipartFile> pictures) throws ParseException {
        Pin pin = getPin(pinId);

        // Title, Location 정보의 변화가 있는가?
        if (request != null) {
            Location updatedLocation = request.getLocation().toEntity();
            locationRepository.save(updatedLocation);
            pin.updateTitle(request.getTitle());
            pin.updateLocation(updatedLocation);
        }

        // 사진 수정. 새로운 사진 목록에 최소 한 장 이상의 사진이 존재해야 한다.
        validatePictureInput(pictures);

        List<Picture> pictureList = uploadAndSavePictures(pictures);
        List<PinPicture> pinPictures = pictureList.stream()
                .map((p) -> PinPicture.builder().pin(pin).picture(p).build())
                .collect(Collectors.toList());
        pin.setPinPictures(pinPictures);

        return new PinDTO.PinDetailResponse(pin);
    }

    @Override
    public void deletePin(Users user, Long pinId) {
        Pin pin = getPin(pinId);
        if (isPinCreatedByUser(user, pin)) {
            // circlePin에서 먼저 삭제한다.
            circlePinRepository.deleteAll(circlePinRepository.findAllByPinId(pinId));

            // 내가 삭제하면 그룹 내에서도 해당 핀이 삭제된다.
            pinRepository.delete(pin);
            user.removePin(pin);
        } else {
            throw new BusinessLogicException("해당 핀에 대한 접근 권한이 없습니다.", ErrorCode.HANDLE_ACCESS_DENIED);
        }
    }

    private List<Picture> uploadAndSavePictures(List<MultipartFile> pictureList) {
        return pictureList.stream().map((p) -> {
            // S3에 사진 업로드
            Map<String, String> result = s3Uploader.upload(p, "static");
            String pictureName = result.get("originalName");
            String uploadUrl = result.get("uploadUrl");

            // Picture 생성
            return pictureRepository.save(Picture.builder().originalName(pictureName).url(uploadUrl).build());
        }).toList();
    }


    private boolean isPinCreatedByUser(Users user, Pin pin) {
        List<Pin> myPins = pinRepository.findByUserId(user.getId());
        return myPins.contains(pin);
    }

    private boolean isPinCreatedByCircle(List<Circle> userJoinCircles, Pin pin) {
        for (Circle circle : userJoinCircles) {
            List<Pin> pinsByCircle = circlePinRepository.findAllPinsByCircleId(circle.getId());
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

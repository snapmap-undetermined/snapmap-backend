package com.project.domain.pin.api;

import com.project.common.exception.EntityNotFoundException;
import com.project.common.exception.ErrorCode;
import com.project.common.exception.InvalidValueException;
import com.project.common.handler.S3Uploader;
import com.project.domain.circle.entity.Circle;
import com.project.domain.circle.repository.CircleRepository;
import com.project.domain.circlepin.entity.CirclePin;
import com.project.domain.circlepin.repository.CirclePinRepository;
import com.project.domain.location.dto.LocationDTO;
import com.project.domain.location.dto.PointDTO;
import com.project.domain.location.entity.Location;
import com.project.domain.location.repository.LocationRepository;
import com.project.domain.picture.entity.Picture;
import com.project.domain.picture.repository.PictureRepository;
import com.project.domain.pin.dto.PinDTO;
import com.project.domain.pin.entity.Pin;
import com.project.domain.pin.repository.PinRepository;
import com.project.domain.pinpicture.entity.PinPicture;
import com.project.domain.pinpicture.repository.PinPictureRepository;
import com.project.domain.users.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PinServiceImpl implements PinService {

    private final PinRepository pinRepository;
    private final PictureRepository pictureRepository;
    private final CircleRepository circleRepository;
    private final CirclePinRepository circlePinRepository;
    private final PinPictureRepository pinPictureRepository;
    private final S3Uploader s3Uploader;
    private final LocationRepository locationRepository;


    @Override
    public PinDTO.PinDetailResponse createPin(Users user, Long circleId, PinDTO.PinCreateRequest request, List<MultipartFile> pictures) {
        try {
            Pin pin = request.toEntity();
            Circle circle = circleRepository.findById(circleId).orElseThrow(
                    () -> new EntityNotFoundException("존재하지 않는 써클입니다."));

            locationRepository.save(pin.getLocation());
            pin.updateUser(user);
            pinRepository.save(pin);

            // Pin - Circle 맵핑 정보 생성
            circlePinRepository.save(CirclePin.builder().circle(circle).pin(pin).build());

            List<Picture> pictureList = uploadAndSavePictures(pictures);

            // Pin - Picture 맵핑 정보 생성
            pictureList.forEach((p) -> pinPictureRepository.save(PinPicture.builder().pin(pin).picture(p).build()));

            return new PinDTO.PinDetailResponse(pin, pictureList);
        } catch (ParseException e) {
            throw new InvalidValueException("핀을 생성하는 데 실패했습니다.", ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    @Override
    public PinDTO.PinDetailResponse getPinDetail(Users user, Long pinId) {
        Pin pin = pinRepository.findById(pinId).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 핀입니다."));

        List<Picture> pictureList = pinPictureRepository.findAllPicturesByPinId(pinId);
        return new PinDTO.PinDetailResponse(pin, pictureList);
    }

    @Override
    public PinDTO.PinDetailListResponse getAllPinsByCircle(Long circleId) {

        // Pin을 모두 조회하고, 각 Pin에 존재하는 사진을 가져온다.
        List<Pin> allPins = pinRepository.findAllByCircleId(circleId);
        List<PinDTO.PinDetailResponse> pinDetailResponseList = allPins.stream().map((pin) -> {
            List<Picture> pictureList = pinPictureRepository.findAllPicturesByPinId(pin.getId());
            return new PinDTO.PinDetailResponse(pin, pictureList);
        }).toList();

        return new PinDTO.PinDetailListResponse(pinDetailResponseList);
    }

    @Override
    public PinDTO.PinDetailListResponse getAllPinByMe(Users user) {
        List<Pin> allPins = pinRepository.findByUserId(user.getId());
        List<PinDTO.PinDetailResponse> pinDetailResponseList = allPins.stream().map((pin) -> {
            List<Picture> pictureList = pinPictureRepository.findAllPicturesByPinId(pin.getId());
            return new PinDTO.PinDetailResponse(pin, pictureList);
        }).toList();

        return new PinDTO.PinDetailListResponse(pinDetailResponseList);

    }

    @Override
    public PinDTO.PinDetailResponse updatePin(Users user, PinDTO.PinUpdateRequest request) throws ParseException {
        Long pinId = request.getPinId();
        Pin pin = pinRepository.findById(pinId).orElseThrow(() -> {
            log.error("Update pin failed. pinId = {} does not exist.", pinId);
            throw new EntityNotFoundException("존재하지 않는 핀입니다.");
        });

        pin.updateTitle(request.getTitle());
        pin.updateLocation(request.getLocationDTO().toEntity());

        // 사진 수정
        List<MultipartFile> newPictureList = request.getPictures();

        // PinPicture에서 기존 매핑 삭제하고 새롭게 추가한다.
        pinPictureRepository.deleteAll(pinPictureRepository.findAllByPinId(pinId));

        List<Picture> pictureList = uploadAndSavePictures(newPictureList);

        // Pin - Picture 맵핑 정보 생성
        pictureList.forEach((p) -> pinPictureRepository.save(PinPicture.builder().pin(pin).picture(p).build()));

        return new PinDTO.PinDetailResponse(pin, pictureList);
    }

    @Override
    public void deletePin(Users user, Long pinId) {
        // PinPicture, circlePin에서 먼저 삭제한다.
        // 내가 삭제하면 그룹 내에서도 삭제된다.
        pinPictureRepository.deleteAll(pinPictureRepository.findAllByPinId(pinId));
        circlePinRepository.deleteAll(circlePinRepository.findAllByPinId(pinId));
        pinRepository.delete(
            pinRepository.findById(pinId).orElseThrow(() -> {
                log.error("Delete pin failed. pinId = {}", pinId);
                throw new EntityNotFoundException("존재하지 않는 핀입니다.");
            })
        );
    }

    public List<Picture> uploadAndSavePictures(List<MultipartFile> pictureList) {
        return pictureList.stream().map((p) -> {
            // S3에 사진 업로드
            Map<String, String> result = s3Uploader.upload(p, "static");
            String pictureName = result.get("originalName");
            String uploadUrl = result.get("uploadUrl");

            // Picture 생성
            return pictureRepository.save(Picture.builder().originalName(pictureName).url(uploadUrl).build());
        }).toList();
    }
}

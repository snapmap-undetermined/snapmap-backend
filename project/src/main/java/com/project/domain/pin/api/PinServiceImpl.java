package com.project.domain.pin.api;

import com.project.common.exception.EntityNotFoundException;
import com.project.common.exception.ErrorCode;
import com.project.common.exception.InvalidValueException;
import com.project.common.handler.S3Uploader;
import com.project.domain.circle.entity.Circle;
import com.project.domain.circle.repository.CircleRepository;
import com.project.domain.circlepin.entity.CirclePin;
import com.project.domain.circlepin.repository.CirclePinRepository;
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
import org.locationtech.jts.io.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    @Override
    public PinDTO.PinDetailResponse createPin(Users user, Long circleId, PinDTO.PinCreateRequest request, List<MultipartFile> pictures) {
        try {
            Pin pin = request.toEntity();
            Circle circle = circleRepository.findById(circleId).orElseThrow(
                    () -> new EntityNotFoundException("존재하지 않는 써클입니다."));

            pin.setUser(user);
            pinRepository.save(pin);

            for (MultipartFile picture : pictures) {
                // S3에 사진 업로드
                Map<String, String> result = s3Uploader.upload(picture, "static");
                String pictureName = result.get("originalName");
                String uploadUrl = result.get("uploadUrl");

                // Picture 생성
                Picture savedPicture = pictureRepository.save(Picture.builder().originalName(pictureName).url(uploadUrl).build());
                pin.addPicture(savedPicture);

                // Pin - Picture 맵핑 정보 생성
                pinPictureRepository.save(PinPicture.builder().pin(pin).picture(savedPicture).build());

                // Pin - Circle 맵핑 정보 생성
                circlePinRepository.save(CirclePin.builder().circle(circle).pin(pin).build());
            }

            return new PinDTO.PinDetailResponse(pin);
        } catch (ParseException e) {
            throw new InvalidValueException("핀을 생성하는 데 실패했습니다.", ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    @Override
    public PinDTO.PinDetailResponse getPinDetail(Users user, Long pinId) {
        Pin pin = pinRepository.findById(pinId).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 핀입니다."));
        return new PinDTO.PinDetailResponse(pin);
    }

    @Override
    public List<PinDTO.PinDetailResponse> getAllPinByCircle(Long circleId) {
        return pinRepository.findAllByCircleId(circleId).stream().map(PinDTO.PinDetailResponse::new).collect(Collectors.toList());
    }

    @Override
    public List<PinDTO.PinDetailResponse> getAllPinByMe(Users user) {
        return pinRepository.findByUserId(user.getId()).stream().map(PinDTO.PinDetailResponse::new).collect(Collectors.toList());
    }

    @Override
    public PinDTO.PinDetailResponse updatePin(Users user, PinDTO.PinUpdateRequest request) throws ParseException {
        Long pinId = request.getPinId();
        Pin pin = pinRepository.findById(pinId).orElseThrow(() -> {
            log.error("Update pin failed. pinId = {} does not exist.", pinId);
            throw new EntityNotFoundException("존재하지 않는 핀입니다.");
        });

        pin.setLocation(PinDTO.toPoint(request.getLocation()));

        // 사진 수정
        List<MultipartFile> newPictureList = request.getPictures();

        pin.getPictureList().clear();

        newPictureList.forEach((picture) -> {
            Map<String, String> result = s3Uploader.upload(picture, "static");
            String pictureName = result.get("originalName");
            String uploadUrl = result.get("uploadUrl");

            pin.addPicture(Picture.builder().originalName(pictureName).url(uploadUrl).build());
        });

        return new PinDTO.PinDetailResponse(pin);
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
}

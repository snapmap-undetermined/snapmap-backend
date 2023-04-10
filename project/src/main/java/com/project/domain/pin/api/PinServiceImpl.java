package com.project.domain.pin.api;

import com.project.common.exception.BusinessLogicException;
import com.project.common.exception.EntityNotFoundException;
import com.project.common.exception.ErrorCode;
import com.project.common.handler.S3Uploader;
import com.project.domain.group.entity.GroupData;
import com.project.domain.group.repository.GroupRepository;
import com.project.domain.location.entity.Location;
import com.project.domain.location.repository.LocationRepository;
import com.project.domain.picture.entity.Picture;
import com.project.domain.picture.repository.PictureRepository;
import com.project.domain.pin.dto.PinDTO;
import com.project.domain.pin.entity.Pin;
import com.project.domain.pin.repository.PinRepository;
import com.project.domain.pintag.entity.PinTag;
import com.project.domain.tag.entity.Tag;
import com.project.domain.tag.repository.TagRepository;
import com.project.domain.usergroup.repository.UserGroupRepository;
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
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final S3Uploader s3Uploader;
    private final LocationRepository locationRepository;
    private final TagRepository tagRepository;
    private final PictureRepository pictureRepository;


    @Override
    @Transactional
    public PinDTO.PinDetailResponse createPin(Users user, Long groupId, PinDTO.PinCreateRequest request, List<MultipartFile> pictures) {
        GroupData group = getGroup(groupId);
        validatePictureInput(pictures);
        validateUserMembershipOnGroup(user, group);

        Pin pin = request.toEntity();
        user.addPin(pin); // 유저에 핀 추가
        group.addPin(pin); // 그룹에 핀 추가

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

        List<GroupData> userJoinGroups = groupRepository.findAllGroupByUserId(user.getId());
        checkPinAccessibility(user, userJoinGroups, pin);

        return new PinDTO.PinDetailResponse(pin);
    }

    @Override
    public PinDTO.PinDetailListResponse getAllPinsByGroup(Long groupId) {
        // Pin을 모두 조회하고, 각 Pin에 존재하는 사진을 가져온다.
        List<Pin> allPins = pinRepository.findAllByGroupId(groupId);
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
            pin.getGroup().removePin(pin); // 써클에서 해당 핀 삭제
            user.removePin(pin); // 유저에서 해당 핀 삭제
            pin.setActivated(pin.getActivated());
        } else {
            throw new BusinessLogicException("Pin access failed..", ErrorCode.ACCESS_DENIED);
        }
    }

    @Override
    public PinDTO.PinWithDistinctPictureResponse getPictureDetail(Users user, Long pictureId) {

        Picture picture = pictureRepository.findById(pictureId).orElseThrow(() -> {
            throw new EntityNotFoundException("Picture does not exists.");
        });

        return new PinDTO.PinWithDistinctPictureResponse(picture);
    }

    private boolean isPinCreatedByUser(Users user, Pin pin) {
        List<Pin> myPins = pinRepository.findByUserId(user.getId());
        return myPins.contains(pin);
    }

    private boolean isPinCreatedByGroup(List<GroupData> userJoinGroups, Pin pin) {
        for (GroupData group : userJoinGroups) {
            List<Pin> pinsByGroup = pinRepository.findAllByGroupId(group.getId());
            if (pinsByGroup.contains(pin)) {
                return true;
            }
        }
        return false;
    }

    private void checkPinAccessibility(Users user, List<GroupData> userJoinGroups, Pin pin) {
        if (!isPinCreatedByUser(user, pin) && !isPinCreatedByGroup(userJoinGroups, pin)) {
            throw new BusinessLogicException("Pin access failed.", ErrorCode.ACCESS_DENIED);
        }
    }

    private void validatePictureInput(List<MultipartFile> pictures) {
        if (pictures == null || pictures.isEmpty()) {
            throw new BusinessLogicException("No available pictures for creating pin.", ErrorCode.INVALID_INPUT_VALUE);
        }
    }

    private void validateUserMembershipOnGroup(Users user, GroupData group) {
        if (userGroupRepository.findByUserIdAndGroupId(user.getId(), group.getId()).isEmpty()) {
            throw new BusinessLogicException("Access denied. Not joined group.", ErrorCode.ACCESS_DENIED);
        }
    }

    private GroupData getGroup(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(
                () -> new EntityNotFoundException("Group does not exists."));
    }

    private Pin getPin(Long pinId) {
        return pinRepository.findById(pinId).orElseThrow(
                () -> new EntityNotFoundException("Pin does not exists."));
    }
}

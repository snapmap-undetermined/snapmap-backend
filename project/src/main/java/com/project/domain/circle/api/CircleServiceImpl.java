package com.project.domain.circle.api;

import com.project.common.exception.EntityNotFoundException;
import com.project.common.exception.ErrorCode;
import com.project.domain.circle.dto.CircleDTO;
import com.project.domain.circle.entity.Circle;
import com.project.domain.circle.repository.CircleRepository;
import com.project.domain.pin.repository.PinRepository;
import com.project.domain.pinpicture.repository.PinPictureRepository;
import com.project.domain.pinpicture.repository.PinPictureRepositoryCustomImpl;
import com.project.domain.usercircle.entity.UserCircle;
import com.project.domain.usercircle.repository.UserCircleRepository;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CircleServiceImpl implements CircleService {

    private final UserCircleRepository userCircleRepository;
    private final CircleRepository circleRepository;
    private final UserRepository userRepository;
    private final PinRepository pinRepository;
    private final PinPictureRepository pinPictureRepository;

    @Override
    @Transactional
    public CircleDTO.CircleSimpleResponse createCircle(Users user, CircleDTO.CreateCircleRequest createCircleRequest) {
        Circle circle = createCircleRequest.toEntity();
        circleRepository.save(circle);

        // userCircle 에 반영이 되어야 한다.
        UserCircle userCircle = UserCircle.builder().user(user).circle(circle).build();
        userCircleRepository.save(userCircle);

        return new CircleDTO.CircleSimpleResponse(circle);

    }

    @Override
    public CircleDTO.CircleSimpleInfoListResponse getAllCircleByUser(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            log.error("Get circle list by user failed. userId={}", userId);
            throw new EntityNotFoundException("존재하지 않는 유저입니다.");
        }

        List<Circle> circleList = userCircleRepository.findAllByUserId(userId);

        List<CircleDTO.CircleSimpleResponse> response = circleList.stream().map(CircleDTO.CircleSimpleResponse::new).collect(Collectors.toList());

        return new CircleDTO.CircleSimpleInfoListResponse(response);

    }

    @Override
    public CircleDTO.CircleDetailResponse getCircleDetail(Long circleId) {
        Circle circle = circleRepository.findById(circleId).orElseThrow();
        int userTotalCount = userCircleRepository.userTotalCountByCircleId(circleId);
        int pinTotalCount = pinRepository.pinTotalCountByCircleId(circleId);
        int pictureTotalCount = pinPictureRepository.pictureTotalCountByCircleId(circleId);

        return new CircleDTO.CircleDetailResponse(circle, userTotalCount, pinTotalCount, pictureTotalCount);
    }

    @Override
    public CircleDTO.CircleWithJoinUserResponse getAllUserByCircle(Long circleId) {

        List<Users> userList = userCircleRepository.findAllUserByCircleId(circleId);
        Circle circle = circleRepository.findById(circleId).orElseThrow(() -> {
            log.error("Get circle failed. circleId={}", circleId);
            throw new EntityNotFoundException("존재하지 않는 그룹입니다.");
        });
        return new CircleDTO.CircleWithJoinUserResponse(userList, circle);
    }

    @Override
    public Long leaveCircle(Long userId, Long circleId) {

        return userCircleRepository.deleteByUserIdAndCircleId(userId, circleId);
    }

    @Override
    public CircleDTO.JoinCircleResponse joinCircle(Users user, CircleDTO.JoinCircleRequest request) {
        Circle circle = circleRepository.findById(request.getCircleId()).orElseThrow(() -> {
            log.error("Join circle failed. circleId = {}", request.getCircleId());
            throw new EntityNotFoundException(ErrorCode.CIRCLENAME_DUPLICATION.getMessage());
        });
        UserCircle userCircle = request.toEntity(user, circle);
        userCircleRepository.save(userCircle);

        return new CircleDTO.JoinCircleResponse(userCircle);
    }

    @Override
    @Transactional
    public CircleDTO.CircleSimpleResponse updateCircleName(Long circleId, CircleDTO.UpdateCircleRequest request) {
        Circle circle = circleRepository.findById(circleId).orElseThrow(() -> {
            log.error("Update circle name failed. circleId = {}", circleId);
            throw new EntityNotFoundException(ErrorCode.CIRCLENAME_DUPLICATION.getMessage());
        });

        circle.setName(request.getCircleName());
        circle.setImageUrl(request.getImageUrl());

        return new CircleDTO.CircleSimpleResponse(circle);
    }
}

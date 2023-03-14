package com.project.domain.circle.api;

import com.project.common.exception.EntityNotFoundException;
import com.project.common.exception.ErrorCode;
import com.project.domain.circle.dto.CircleDTO;
import com.project.domain.circle.entity.Circle;
import com.project.domain.circle.repository.CircleRepository;
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
public class CircleServiceImpl implements CircleService {

    private final UserCircleRepository userCircleRepository;
    private final CircleRepository circleRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CircleDTO.CircleSimpleInfoResponse createCircle(Users user, CircleDTO.CreateCircleRequest createCircleRequest) {
        Circle circle = createCircleRequest.toEntity();

        // userCircle에도 반영이 되어야 한다.
        UserCircle userCircle = UserCircle.builder().user(user).circle(circle).build();
        circle.addUserCircle(userCircle);
        circleRepository.save(circle);

        return new CircleDTO.CircleSimpleInfoResponse(circle);

    }

    @Override
    public CircleDTO.CircleSimpleInfoListResponse getCircleListByUser(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            log.error("Get circle list by user failed. userId={}", userId);
            throw new EntityNotFoundException("존재하지 않는 유저입니다.");
        }

        List<Circle> circleList = circleRepository.findAllCircleByUserId(userId);

        List<CircleDTO.CircleSimpleInfoResponse> response = circleList.stream().map(CircleDTO.CircleSimpleInfoResponse::new).collect(Collectors.toList());

        return new CircleDTO.CircleSimpleInfoListResponse(response);

    }

    @Override
    public CircleDTO.CircleWithJoinUserResponse getUserListByCircle(Long circleId) {

        List<Users> userList = circleRepository.findAllUserByCircleId(circleId);
        Circle circle = circleRepository.findById(circleId).orElseThrow(()->{
            log.error("Get circle failed. circleId={}", circleId);
            throw new EntityNotFoundException("존재하지 않는 그룹입니다.");
        });
        return new CircleDTO.CircleWithJoinUserResponse(userList, circle);
    }

    @Override
    public CircleDTO.CircleSimpleInfoResponse leaveCircle(Long userId, Long circleId) {
        UserCircle userCircle = userCircleRepository.findByUserIdAndCircleId(userId, circleId).orElseThrow();
        Circle circle = circleRepository.findById(circleId).orElseThrow();
        circle.removeUserCircle(userCircle);

        return new CircleDTO.CircleSimpleInfoResponse(circle);
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
    public CircleDTO.CircleSimpleInfoResponse updateCircleName(Long circleId, CircleDTO.UpdateCircleRequest request) {
        Circle circle = circleRepository.findById(circleId).orElseThrow(() -> {
            log.error("Update circle name failed. circleId = {}", circleId);
            throw new EntityNotFoundException(ErrorCode.CIRCLENAME_DUPLICATION.getMessage());
        });
        circle.setName(request.getCircleName());

        return new CircleDTO.CircleSimpleInfoResponse(circle);
    }
}

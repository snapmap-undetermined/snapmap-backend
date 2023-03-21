package com.project.domain.circle.api;

import com.project.common.exception.EntityNotFoundException;
import com.project.common.exception.ErrorCode;
import com.project.common.handler.S3Uploader;
import com.project.domain.circle.dto.CircleDTO;
import com.project.domain.circle.entity.Circle;
import com.project.domain.circle.repository.CircleRepository;
import com.project.domain.picture.entity.Picture;
import com.project.domain.usercircle.entity.UserCircle;
import com.project.domain.usercircle.repository.UserCircleRepository;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
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
    private final S3Uploader s3Uploader;

    @Override
    @Transactional
    public CircleDTO.CircleSimpleInfoResponse createCircle(Users user, CircleDTO.CreateCircleRequest request) {
        Circle circle = request.toEntity();
        circle.setCircleKey(circle.generateCircleKey());
        circle.setMaster(user);

        UserCircle userCircle = UserCircle.builder().user(user).activated(true).circle(circle).build();
        userCircle.addUserCircleToUserAndCircle(user, circle);

        // 그룹 생성 시, 친구를 같이 초대하는 경우 처리
        if (request.getInvitedUserList() != null && !request.getInvitedUserList().isEmpty()) {
            request.getInvitedUserList().forEach((userId) -> {
                Users u = userRepository.findById(userId).orElseThrow();
                UserCircle uc = UserCircle.builder().circle(circle).activated(false).user(u).build();
                uc.addUserCircleToUserAndCircle(u, circle);
                userCircleRepository.save(uc);
            });
        }
        circleRepository.save(circle);

        return new CircleDTO.CircleSimpleInfoResponse(circle);
    }

    @Override
    public CircleDTO.CircleSimpleInfoListResponse getAllCircleByUser(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            log.error("Get circle list by user failed. userId={}", userId);
            throw new EntityNotFoundException("존재하지 않는 유저입니다.");
        }

        List<Circle> circleList = circleRepository.findAllCircleByUserId(userId);

        List<CircleDTO.CircleSimpleInfoResponse> response = circleList.stream().map(CircleDTO.CircleSimpleInfoResponse::new).collect(Collectors.toList());

        return new CircleDTO.CircleSimpleInfoListResponse(response);

    }

    @Override
    public CircleDTO.CircleDetailInfoResponse getCircle(Long circleId) {

        List<Users> userList = circleRepository.findAllUserByCircleId(circleId);
        Circle circle = circleRepository.findById(circleId).orElseThrow(() -> {
            log.error("Get circle failed. circleId={}", circleId);
            throw new EntityNotFoundException("존재하지 않는 그룹입니다.");
        });

        return new CircleDTO.CircleDetailInfoResponse(userList, circle);
    }

    @Override
    public CircleDTO.CircleWithJoinUserResponse getJoinedUserOfCircle(Long circleId) {

        List<Users> userList = circleRepository.findAllUserByCircleId(circleId);
        Circle circle = circleRepository.findById(circleId).orElseThrow(() -> {
            log.error("Get circle failed. circleId={}", circleId);
            throw new EntityNotFoundException("존재하지 않는 그룹입니다.");
        });
        return new CircleDTO.CircleWithJoinUserResponse(userList, circle);
    }

    // 본인이 스스로 나감
    // TODO: 방장이 나갈 경우 고려
    @Override
    @Transactional
    public CircleDTO.CircleSimpleInfoResponse leaveCircle(Users user, Long circleId) {
        Circle circle = circleRepository.findById(circleId).orElseThrow(() -> {
            log.error("Get circle failed. circleId={}", circleId);
            throw new EntityNotFoundException("존재하지 않는 그룹입니다.");
        });

        UserCircle userCircle = userCircleRepository.findByUserIdAndCircleId(user.getId(), circle.getId()).orElseThrow();
        userCircle.removeUserCircleFromUserAndCircle(user, circle);

        return new CircleDTO.CircleSimpleInfoResponse(circle);
    }

    // 방장이 유저를 추방함
    @Override
    @Transactional
    public CircleDTO.CircleSimpleInfoResponse banUserFromCircle(Users user, Long circleId, CircleDTO.BanUserRequest banUserRequest) {

        Circle circle = circleRepository.findById(circleId).orElseThrow();
        // 방장 권한일 경우
        if (isMasterUser(circle, user.getId())) {
            UserCircle userCircle = userCircleRepository.findByUserIdAndCircleId(banUserRequest.getUserId(), circleId).orElseThrow();
            userCircle.removeUserCircleFromUserAndCircle(user, circle);
        }
        return new CircleDTO.CircleSimpleInfoResponse(circle);
    }

    // 그룹에 유저를 초대
    @Override
    @Transactional
    public CircleDTO.InviteUserResponse inviteUser(Users user, Long circleId, CircleDTO.InviteUserRequest request) {
        Circle circle = circleRepository.findById(circleId).orElseThrow(() -> {
            log.error("Join circle failed. circleId = {}", circleId);
            throw new EntityNotFoundException(ErrorCode.CIRCLENAME_DUPLICATION.getMessage());
        });

        List<Long> invitedUserList = request.getInvitedUserList();

        invitedUserList.forEach((userId) -> {
            Users u = userRepository.findById(userId).orElseThrow(()->{
                log.error("Get user failed. userId={}", userId);
                throw new EntityNotFoundException("존재하지 않는 유저입니다.");
            });
            UserCircle uc = UserCircle.builder().user(u).circle(circle).activated(false).build(); // activated = false : 수락 이전 상태
            uc.addUserCircleToUserAndCircle(u, circle);
            userCircleRepository.save(uc);
        });

        return new CircleDTO.InviteUserResponse(circle);
    }

    // 유저가 초대요청을 수락
    @Override
    @Transactional
    public CircleDTO.AllowUserJoinResponse allowUserJoin(Users user, Long circleId) {
        UserCircle userCircle = userCircleRepository.findByUserIdAndCircleId(user.getId(), circleId).orElseThrow();

        userCircle.setActivated(userCircle.getActivated());
        return new CircleDTO.AllowUserJoinResponse(user, userCircle);
    }

    @Override
    @Transactional
    public CircleDTO.CircleSimpleInfoResponse updateCircle(Users user, Long circleId, CircleDTO.UpdateCircleRequest request, MultipartFile picture) {
        Circle circle = circleRepository.findById(circleId).orElseThrow(() -> {
            log.error("Update circle name failed. circleId = {}", circleId);
            throw new EntityNotFoundException(ErrorCode.CIRCLENAME_DUPLICATION.getMessage());
        });
        if (isMasterUser(circle, user.getId())) {
            circle.setName(request.getCircleName());

            // TODO : 예외 처리 필요함
            String imageUrl = s3Uploader.uploadAndSaveImage(picture);
            circle.setImageUrl(imageUrl);
        }
        return new CircleDTO.CircleSimpleInfoResponse(circle);
    }

    private boolean isMasterUser(Circle circle, Long userId) {
        return circle.getMaster().getId().equals(userId);
    }

}

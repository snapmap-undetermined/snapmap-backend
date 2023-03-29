package com.project.domain.circle.api;

import com.project.common.exception.BusinessLogicException;
import com.project.common.exception.EntityNotFoundException;
import com.project.common.exception.ErrorCode;
import com.project.common.handler.S3Uploader;
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
import org.springframework.web.multipart.MultipartFile;

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
    public CircleDTO.CircleDetailInfoResponse getCircleDetail(Long circleId) {

        Circle circle = getCircle(circleId);

        return new CircleDTO.CircleDetailInfoResponse(circle);
    }

    @Override
    public CircleDTO.CircleWithJoinUserResponse getJoinedUserOfCircle(Long circleId) {

        Circle circle = getCircle(circleId);
        return new CircleDTO.CircleWithJoinUserResponse(circle);
    }

    // 본인이 스스로 나감
    // TODO: 방장이 나갈 경우 고려
    @Override
    @Transactional
    public CircleDTO.CircleSimpleInfoResponse leaveCircle(Users user, Long circleId) {
        Circle circle = getCircle(circleId);
        if (circle.getMaster().getId().equals(user.getId())) {
            throw new BusinessLogicException("방장 권한을 가진 유저는 그룹에서 나갈 수 없습니다.", ErrorCode.INTERNAL_SERVER_ERROR);
        }

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
        Circle circle = getCircle(circleId);

        List<Long> invitedUserList = request.getInvitedUserList();

        invitedUserList.forEach((userId) -> {
            Users u = userRepository.findById(userId).orElseThrow(() -> {
                log.error("Get user failed. userId={}", userId);
                throw new EntityNotFoundException("존재하지 않는 유저입니다.");
            });
            UserCircle uc = UserCircle.builder().user(u).circle(circle).activated(false).build(); // activated = false : 수락 이전 상태
            uc.addUserCircleToUserAndCircle(u, circle);
            userCircleRepository.save(uc);
        });

        return new CircleDTO.InviteUserResponse(circle);
    }

    // 앱 설치 후 유저가 링크를 타고 들어올 경우, 로그인을 완료했을 경우 그룹 초대를 자동으로 한다.
    @Override
    public CircleDTO.InviteUserFromLinkResponse inviteUserFromLink(Users user, String circleKey) {
        Circle circle = circleRepository.findCircleByKey(circleKey);
        UserCircle userCircle = UserCircle.builder().circle(circle).user(user).activated(false).build();
        userCircleRepository.save(userCircle);

        return new CircleDTO.InviteUserFromLinkResponse(userCircle);
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
        Circle circle = getCircle(circleId);
        if (isMasterUser(circle, user.getId())) {
            circle.setName(request.getCircleName());
            circle.setDescription(request.getDescription());
            if (picture != null && !picture.isEmpty()) {
                String imageUrl = s3Uploader.uploadAndSaveImage(picture);
                circle.setImageUrl(imageUrl);
            }
        } else {
            throw new BusinessLogicException("방장만 그룹 설정을 수정할 수 있습니다.", ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return new CircleDTO.CircleSimpleInfoResponse(circle);
    }

    // 유저가 방장일 경우, 방장 권한을 위임한다.
    @Override
    @Transactional
    public CircleDTO.CircleWithJoinUserResponse updateCircleMaster(Users user, Long circleId, Long userId) {
        Circle circle = getCircle(circleId);
        if (isMasterUser(circle, user.getId())) {
            Users u = userRepository.findById(userId).orElseThrow();
            circle.setMaster(u);
        }
        return new CircleDTO.CircleWithJoinUserResponse(circle);
    }

    private boolean isMasterUser(Circle circle, Long userId) {
        return circle.getMaster().getId().equals(userId);
    }

    private Circle getCircle(Long circleId) {
        return circleRepository.findById(circleId).orElseThrow(() -> {
            log.error("Get circle failed. circleId = {}", circleId);
            throw new EntityNotFoundException("존재하지 않는 그룹 입니다.");
        });
    }

}

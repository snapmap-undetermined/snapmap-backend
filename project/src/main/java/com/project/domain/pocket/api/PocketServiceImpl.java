package com.project.domain.pocket.api;

import com.project.common.exception.BusinessLogicException;
import com.project.common.exception.EntityNotFoundException;
import com.project.common.exception.ErrorCode;
import com.project.common.handler.S3Uploader;
import com.project.domain.picture.entity.Picture;
import com.project.domain.pocket.dto.PocketDTO;
import com.project.domain.pocket.entity.Pocket;
import com.project.domain.pocket.repository.PocketRepository;
import com.project.domain.userpocket.entity.UserPocket;
import com.project.domain.userpocket.repository.UserPocketRepository;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PocketServiceImpl implements PocketService {

    private final UserPocketRepository userPocketRepository;
    private final PocketRepository pocketRepository;
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    @Override
    @Transactional
    public PocketDTO.PocketSimpleInfoResponse createPocket(Users user, PocketDTO.CreatePocketRequest request) {
        Pocket pocket = request.toEntity();
        pocket.setPocketKey(pocket.generatePocketKey());
        pocket.setMaster(user);

        UserPocket userPocket = UserPocket.builder().user(user).activated(true).pocket(pocket).build();
        userPocket.addUserPocketToUserAndPocket(user, pocket);

        // 포켓 생성 시, 친구를 같이 초대하는 경우 처리
        if (request.getInvitedUserList() != null) {
            request.getInvitedUserList().forEach((userId) -> {
                Users u = userRepository.findById(userId).orElse(null);
                if (u == null) {
                    log.error("create pocket with friend, no userId : {}", userId);
                }
                UserPocket uc = UserPocket.builder().pocket(pocket).activated(false).user(u).build();
                uc.addUserPocketToUserAndPocket(u, pocket);
                log.info("create pocket with friend, userId : {}", userId);
                userPocketRepository.save(uc);
            });
        }
        Pocket saved = pocketRepository.save(pocket);
        log.info("Pocket created, pocket : {}", pocket);

        return new PocketDTO.PocketSimpleInfoResponse(pocket);
    }

    @Override
    public PocketDTO.PocketSimpleInfoListResponse getAllPocketByUser(Long userId) {
        Users user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            log.error("getAllPocketByUser error. No user by userId : {}", userId);
            throw new EntityNotFoundException("User does not exist.");
        }

        List<Pocket> pocketList = pocketRepository.findAllPocketByUserId(userId);
        log.info("pocketList by userId({}) : {}", userId, pocketList);
        List<PocketDTO.PocketSimpleInfoResponse> response = pocketList.stream().map(PocketDTO.PocketSimpleInfoResponse::new).collect(Collectors.toList());

        return new PocketDTO.PocketSimpleInfoListResponse(response);

    }

    @Override
    public PocketDTO.PocketDetailInfoResponse getPocketDetail(Long pocketId) {
        Pocket pocket = pocketRepository.findById(pocketId).orElse(null);
        if (pocket == null) {
            log.info("No pocket, pocketId : {}", pocketId);
            throw new EntityNotFoundException("Group does not exists.");
        }
        return new PocketDTO.PocketDetailInfoResponse(pocket);
    }

    @Override
    public PocketDTO.PocketWithJoinUserResponse getJoinedUserOfPocket(Long pocketId) {
        Pocket pocket = pocketRepository.findById(pocketId).orElse(null);
        if (pocket == null) {
            log.info("No pocket, pocketId : {}", pocketId);
            throw new EntityNotFoundException("Group does not exists.");
        }
        return new PocketDTO.PocketWithJoinUserResponse(pocket);
    }

    // 본인이 스스로 나감
    @Override
    @Transactional
    public PocketDTO.PocketSimpleInfoResponse leavePocket(Users user, Long pocketId) {
        Pocket pocket = pocketRepository.findById(pocketId).orElse(null);
        if (pocket == null) {
            log.info("No pocket, pocketId : {}", pocketId);
            throw new EntityNotFoundException("Pocket does not exists");
        }

        if (isMasterUser(pocket, user.getId()) && pocket.getUserPocketList().size() > 1) {
            throw new BusinessLogicException("Manager cannot leave group", ErrorCode.POCKET_MANAGER_ERROR);
        }

        // 유저가 혼자 남았을 경우에 포켓을 나가게 되면 해당 포켓이 삭제된다.
        if (pocket.getUserPocketList().size() == 1) {
            UserPocket userPocket = userPocketRepository.findByUserIdAndPocketId(user.getId(), pocket.getId()).orElseThrow();
            userPocket.removeUserPocketFromUserAndPocket(user, pocket);
            pocketRepository.delete(pocket);
        } else {
            UserPocket userPocket = userPocketRepository.findByUserIdAndPocketId(user.getId(), pocket.getId()).orElseThrow();
            userPocket.removeUserPocketFromUserAndPocket(user, pocket);
        }

        return new PocketDTO.PocketSimpleInfoResponse(pocket);
    }

    // 방장이 유저를 추방함
    @Override
    @Transactional
    public PocketDTO.PocketSimpleInfoResponse banUserFromPocket(Users user, Long pocketId, PocketDTO.BanUserRequest banUserRequest) {

        Pocket pocket = pocketRepository.findById(pocketId).orElseThrow();
        // 방장 권한일 경우
        if (isMasterUser(pocket, user.getId())) {
            UserPocket userPocket = userPocketRepository.findByUserIdAndPocketId(banUserRequest.getUserId(), pocketId).orElseThrow();
            userPocket.removeUserPocketFromUserAndPocket(user, pocket);
        }
        return new PocketDTO.PocketSimpleInfoResponse(pocket);
    }

    // 포켓에 유저를 초대
    @Override
    @Transactional
    public PocketDTO.InviteUserResponse inviteUser(Users user, Long pocketId, PocketDTO.InviteUserRequest request) {
        Pocket pocket = pocketRepository.findById(pocketId).orElse(null);
        if (pocket == null) {
            log.info("No pocket, pocketId : {}", pocketId);
            throw new EntityNotFoundException("Pocket does not exists");
        }

        List<Long> invitedUserList = request.getInvitedUserList();

        for (Long userId : invitedUserList) {
            Users u = userRepository.findById(userId).orElse(null);
            if (u == null) {
                log.error("No user by userId : {}", userId);
                throw new EntityNotFoundException("User does not exist.");
            }
            Optional<UserPocket> userPocket = userPocketRepository.findByUserIdAndPocketId(u.getId(), pocketId);
            if (userPocket.isPresent()) {
                continue;
            }
            UserPocket uc = UserPocket.builder().user(u).pocket(pocket).activated(false).build(); // activated = false : 수락 이전 상태
            userPocketRepository.save(uc);
        }

        return new PocketDTO.InviteUserResponse(pocket, user);
    }

    // 앱 설치 후 유저가 링크를 타고 들어올 경우, 로그인을 완료했을 경우 포켓 초대를 자동으로 한다.
    @Override
    public PocketDTO.InviteUserFromLinkResponse inviteUserFromLink(Users user, String pocketKey) {
        Pocket pocket = pocketRepository.findPocketByKey(pocketKey);
        UserPocket userPocket = UserPocket.builder().pocket(pocket).user(user).activated(false).build();
        userPocketRepository.save(userPocket);

        return new PocketDTO.InviteUserFromLinkResponse(userPocket);
    }

    // 유저가 초대 요청을 수락
    @Override
    @Transactional
    public PocketDTO.acceptPocketInvitationResponse acceptPocketInvitation(Users user, Long pocketId) {
        Pocket pocket = pocketRepository.findById(pocketId).orElse(null);
        if (pocket == null) {
            log.info("No pocket, pocketId : {}", pocketId);
            throw new EntityNotFoundException("Pocket does not exists");
        }

        UserPocket userPocket = userPocketRepository.findByUserIdAndPocketId(user.getId(), pocketId).orElseThrow();
        userPocket.setActivated(userPocket.getActivated());
        userPocket.addUserPocketToUserAndPocket(user, pocket);

        return new PocketDTO.acceptPocketInvitationResponse(user, userPocket);
    }

    @Override
    @Transactional
    public PocketDTO.cancelInvitePocketResponse cancelPocketInvitation(Users user, Long pocketId, Long cancelUserId) {
        Pocket pocket = pocketRepository.findById(pocketId).orElse(null);
        if (pocket == null) {
            log.info("No pocket, pocketId : {}", pocketId);
            throw new EntityNotFoundException("Pocket does not exists");
        }

        UserPocket userPocket = userPocketRepository.findByUserIdAndPocketId(cancelUserId, pocketId).orElseThrow(() -> {
            throw new EntityNotFoundException("User does not exist.");
        });
        // 요청을 보내는 유저가 해당 포켓에 속해있어야 초대 취소가 가능하다.
        if (pocket.getUserPocketList().stream()
                .filter(UserPocket::getActivated)
                .map(UserPocket::getUser).toList().contains(user)) {
            userPocket.removeUserPocketFromUserAndPocket(user, pocket);
        }

        return new PocketDTO.cancelInvitePocketResponse(user, pocket);
    }

    @Override
    @Transactional
    public PocketDTO.PocketSimpleInfoResponse updatePocket(Users user, Long pocketId, PocketDTO.UpdatePocketRequest request, MultipartFile picture) {
        Pocket pocket = pocketRepository.findById(pocketId).orElse(null);
        if (pocket == null) {
            log.info("No pocket, pocketId : {}", pocketId);
            throw new EntityNotFoundException("Pocket does not exists");
        }

        if (isMasterUser(pocket, user.getId())) {
            pocket.setName(request.getPocketName());
            pocket.setDescription(request.getDescription());
            if (picture != null && !picture.isEmpty()) {
                List<Picture> pictureList = s3Uploader.uploadAndSavePictures(Collections.singletonList(picture));
                pocket.setImageUrl(pictureList.get(0).getUrl());
            }
        } else {
            throw new BusinessLogicException("Only the manager can modify group settings.", ErrorCode.POCKET_MANAGER_ERROR);
        }
        return new PocketDTO.PocketSimpleInfoResponse(pocket);
    }

    // 유저가 방장일 경우, 방장 권한을 위임한다.
    @Override
    @Transactional
    public PocketDTO.PocketWithJoinUserResponse updatePocketMaster(Users user, Long pocketId, Long userId) {
        Pocket pocket = pocketRepository.findById(pocketId).orElse(null);
        if (pocket == null) {
            log.info("No pocket, pocketId : {}", pocketId);
            throw new EntityNotFoundException("Pocket does not exists");
        }

        if (isMasterUser(pocket, user.getId())) {
            Users targetUser = userRepository.findById(userId).orElse(null);
            if (targetUser == null) {
                log.error("No user by userId : {}", userId);
                throw new EntityNotFoundException("User does not exist.");
            }
            log.info("Delegate manager target userId : {}", targetUser.getId());
            // 위임하려는 유저가 해당 포켓에 속해 있어야 한다.
            if (!isUserInPocket(targetUser, pocketId)) {
                throw new EntityNotFoundException("Delegate target user does not exist", ErrorCode.POCKET_MANAGER_ERROR);
            }
            pocket.setMaster(targetUser);
        }
        return new PocketDTO.PocketWithJoinUserResponse(pocket);
    }

    @Override
    public PocketDTO.NotAcceptPocketInviteUserResponse getAllNotAcceptPocketInviteUser(Users user, Long pocketId) {
        Pocket pocket = pocketRepository.findById(pocketId).orElse(null);
        if (pocket == null) {
            log.info("No pocket, pocketId : {}", pocketId);
            throw new EntityNotFoundException("Pocket does not exists");
        }
        return new PocketDTO.NotAcceptPocketInviteUserResponse(pocket);
    }

    private boolean isMasterUser(Pocket pocket, Long userId) {
        return pocket.getMaster().getId().equals(userId);
    }

    private boolean isUserInPocket(Users user, Long pocketId) {
        Pocket pocket = pocketRepository.findById(pocketId).orElse(null);
        if (pocket == null) {
            log.info("No pocket, pocketId : {}", pocketId);
            throw new EntityNotFoundException("Pocket does not exists");
        }

        return pocket.getUserPocketList().stream()
                .map(UserPocket::getUser).toList().contains(user);
    }
}

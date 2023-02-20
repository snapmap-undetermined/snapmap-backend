package com.project.album.domain.circle.api;

import com.project.album.common.exception.BusinessLogicException;
import com.project.album.common.exception.EntityNotFoundException;
import com.project.album.common.exception.ErrorCode;
import com.project.album.common.exception.InvalidValueException;
import com.project.album.domain.circle.dto.CircleDTO;
import com.project.album.domain.circle.entity.Circle;
import com.project.album.domain.circle.repository.CircleRepository;
import com.project.album.domain.usercircle.dto.UserCircleDTO;
import com.project.album.domain.usercircle.entity.UserCircle;
import com.project.album.domain.usercircle.repository.UserCircleRepository;
import com.project.album.domain.users.dto.UserDTO;
import com.project.album.domain.users.entity.Users;
import com.project.album.domain.users.repository.UserRepository;
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
        try {
            Circle circle = createCircleRequest.toEntity();
            circleRepository.save(circle);

            // userCircle에도 반영이 되어야 한다.
            UserCircle userCircle = UserCircle.builder().user(user).circle(circle).build();
            userCircleRepository.save(userCircle);

            return new CircleDTO.CircleSimpleInfoResponse(circle);
        }catch(BusinessLogicException e){
            log.error("Create circle failed. userId= {}, circleName={}", user.getId(), createCircleRequest.getCircleName());
            throw new BusinessLogicException("Create circle failed", ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<CircleDTO.CircleSimpleInfoResponse> getCircleListByUser(Long userId){
        if (userRepository.findById(userId).isEmpty()){
            log.error("Get circle list by user failed. userId={}",userId);
            throw new EntityNotFoundException("존재하지 않는 유저입니다.");
        }
        List<UserCircle> userCircleList = userCircleRepository.findByUserId(userId);

        return userCircleList.stream().map((uc) -> new CircleDTO.CircleSimpleInfoResponse(uc.getCircle())).collect(Collectors.toList());

    }

    @Override
    public CircleDTO.CircleWithJoinUserResponse getUserListByCircle(Long userId, Long circleId) throws Exception {

        List<Users> userList = userCircleRepository.findAllUserByCircleId(userId, circleId);

        return new CircleDTO.CircleWithJoinUserResponse(userList);
    }

    @Override
    public int leaveCircle(Long userId, Long circleId) {

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
    public CircleDTO.CircleSimpleInfoResponse updateCircleName(Long circleId, CircleDTO.UpdateCircleRequest request) {
        Circle circle = circleRepository.findById(circleId).orElseThrow(() -> {
            log.error("Update circle name failed. circleId = {}", circleId);
            throw new EntityNotFoundException(ErrorCode.CIRCLENAME_DUPLICATION.getMessage());
        });
        circle.setName(request.getCircleName());

        return new CircleDTO.CircleSimpleInfoResponse(circle);
    }
}

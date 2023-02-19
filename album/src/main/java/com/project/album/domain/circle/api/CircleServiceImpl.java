package com.project.album.domain.circle.api;

import com.project.album.domain.circle.dto.CircleDTO;
import com.project.album.domain.circle.entity.Circle;
import com.project.album.domain.circle.repository.CircleRepository;
import com.project.album.domain.usercircle.dto.UserCircleDTO;
import com.project.album.domain.usercircle.entity.UserCircle;
import com.project.album.domain.usercircle.repository.UserCircleRepository;
import com.project.album.domain.users.dto.UserDTO;
import com.project.album.domain.users.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CircleServiceImpl implements CircleService {

    private final UserCircleRepository userCircleRepository;
    private final CircleRepository circleRepository;

    @Override
    public CircleDTO.CircleSimpleInfoResponse createCircle(Users user, CircleDTO.CreateCircleRequest createCircleRequest) throws Exception {
        // circle을 생성하고
        Circle circle = createCircleRequest.toEntity();

        Random random = new Random();
        String circleKey = random.ints(48, 122 + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        circle.setCircleKey(circleKey);
        circleRepository.save(circle);

        // userCircle에도 반영이 되어야 한다.
        UserCircle userCircle = UserCircle.builder().user(user).circle(circle).build();
        userCircleRepository.save(userCircle);

        return new CircleDTO.CircleSimpleInfoResponse(circle);
    }

    @Override
    public List<CircleDTO.CircleSimpleInfoResponse> getCircleListByUser(Long userId) throws Exception {

        List<UserCircle> userCircleList = userCircleRepository.findByUserId(userId);

        return userCircleList.stream().map((uc) -> new CircleDTO.CircleSimpleInfoResponse(uc.getCircle())).collect(Collectors.toList());

    }

    @Override
    public List<UserDTO.UserSimpleInfoResponse> getUserListByCircle(Long userId, Long circleId) throws Exception {

        List<Users> userList = userCircleRepository.findUserListByCircleId(userId, circleId);

        return userList.stream().map(UserDTO.UserSimpleInfoResponse::new).collect(Collectors.toList());
    }

    @Override
    public int exitedUser(Long userId, Long circleId) throws Exception {

        return userCircleRepository.exitedUserFromGroup(userId, circleId);
    }

    @Override
    public UserCircleDTO.UserCircleSimpleInfoResponse joinCircleFromLink(Users user, UserCircleDTO.JoinUserCircleFromLinkRequest request) throws Exception {
        Circle circle = circleRepository.findByCircleKey(request.getCircleKey());
        UserCircle userCircle = request.toEntity();
        userCircle.setCircle(circle);
        userCircle.setUser(user);
        userCircleRepository.save(userCircle);

        return new UserCircleDTO.UserCircleSimpleInfoResponse(userCircle);
    }

    @Override
    public UserCircleDTO.UserCircleSimpleInfoResponse joinCircleInApp(Users user, UserCircleDTO.JoinUserCircleInAppRequest request) throws Exception {
        Circle circle = circleRepository.findById(request.getCircleId()).orElseThrow();
        UserCircle userCircle = request.toEntity();
        userCircle.setCircle(circle);
        userCircle.setUser(user);
        userCircleRepository.save(userCircle);

        return new UserCircleDTO.UserCircleSimpleInfoResponse(userCircle);
    }

    @Override
    public CircleDTO.CircleSimpleInfoResponse updateCircleName(Long circleId, CircleDTO.UpdateCircleRequest request) throws Exception {
        Circle circle = circleRepository.findById(circleId).orElseThrow();
        circle.setName(request.getCircleName());
        circleRepository.save(circle);

        return new CircleDTO.CircleSimpleInfoResponse(circle);
    }
}

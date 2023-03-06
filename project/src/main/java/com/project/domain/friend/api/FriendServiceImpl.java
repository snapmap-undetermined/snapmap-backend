package com.project.domain.friend.api;

import com.project.common.exception.BusinessLogicException;
import com.project.common.exception.EntityNotFoundException;
import com.project.common.exception.ErrorCode;
import com.project.domain.friend.dto.FriendDTO;
import com.project.domain.friend.entity.Friend;
import com.project.domain.friend.repository.FriendRepository;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Override
    public List<FriendDTO.FriendSimpleInfoResponse> getAllFriends(Long userId) throws Exception {

        List<Friend> friendList = friendRepository.findAllByUserId(userId);

        return friendList.stream().map(FriendDTO.FriendSimpleInfoResponse::new).collect(Collectors.toList());

    }

    @Override
    @Transactional
    public FriendDTO.FriendSimpleInfoResponse createFriend(Users user, FriendDTO.CreateFriendRequest createFriendRequest) {

        Friend friend = createFriendRequest.toEntity();
        Users friendUser = userRepository.findById(createFriendRequest.getFriendUserId()).orElseThrow(() -> {
            throw new EntityNotFoundException("존재하지 않는 유저입니다.");
        });
        // 유저-친구 정보 동일, 이미 있는 친구관계 중복 확인
        validCheckAboutCreateFriend(user.getId(), friendUser.getId());

        friend.setFriendUser(friendUser);
        friend.setMeUser(user);

        friendRepository.save(friend);

        return new FriendDTO.FriendSimpleInfoResponse(friend);
    }

    @Override
    public void deleteFriend(Long friendId) {

        Friend friend = friendRepository.findById(friendId).orElseThrow(() -> {
            throw new EntityNotFoundException("존재하지 않는 친구관계 입니다.");
        });
        friendRepository.delete(friend);

    }

    @Override
    public FriendDTO.FriendSimpleInfoResponse updateFriendName(Long friendId, FriendDTO.UpdateFriendNameRequest updateFriendNameRequest) {
        Friend friend = friendRepository.findById(friendId).orElseThrow(() -> {
            throw new EntityNotFoundException("존재하지 않는 친구관계 입니다.");
        });
        String updateFriendName = updateFriendNameRequest.getFriendName();
        friend.setFriendName(updateFriendName);

        return new FriendDTO.FriendSimpleInfoResponse(friend);
    }

    void validCheckAboutCreateFriend(Long meUserId, Long friendUserId) {
        if (Objects.equals(meUserId, friendUserId)) {
            throw new BusinessLogicException("유저-친구 정보가 동일합니다.", ErrorCode.REQUEST_USER_ID_VALID_ERROR);
        }
        if (friendRepository.existByMeUserIdAndFriendUserId(meUserId, friendUserId)) {
            throw new BusinessLogicException("이미 있는 친구관계 입니다.", ErrorCode.FRIEND_DUPLICATION);
        }
    }
}

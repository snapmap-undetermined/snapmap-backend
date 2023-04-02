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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Override
    public FriendDTO.FriendListResponse getAllFriends(Long userId) {

        List<FriendDTO.FriendResponse> friendList = friendRepository.findAllFriendsOfUser(userId);
        return new FriendDTO.FriendListResponse(friendList);
    }

    @Override
    @Transactional
    public FriendDTO.FriendResponse createFriend(Users user, FriendDTO.CreateFriendRequest createFriendRequest) {
        Users mate = userRepository.findById(createFriendRequest.getFriendUserId()).orElseThrow(() -> {
            throw new EntityNotFoundException("존재하지 않는 유저입니다.");
        });

        // 유저-친구 정보 동일, 이미 있는 친구관계 중복 확인
        if (Objects.equals(user.getId(), mate.getId())) {
            throw new BusinessLogicException("유저-친구 정보가 동일합니다.", ErrorCode.REQUEST_USER_ID_VALID_ERROR);
        }
        if (friendRepository.existsByUserIds(user.getId(), mate.getId())) {
            throw new BusinessLogicException("이미 있는 친구관계 입니다.", ErrorCode.FRIEND_DUPLICATION);
        }

        Friend friend = Friend.builder().me(user).mate(mate).friendName(mate.getNickname()).build();
        friendRepository.save(friend);
        return new FriendDTO.FriendResponse(friend);
    }

    @Override
    @Transactional
    public FriendDTO.FriendResponse deleteFriend(Users me, Long mateId) {
        Long myId = me.getId();
        Friend friend = friendRepository.findByMeIdAndMateId(myId, mateId).orElseThrow(() -> {
            throw new EntityNotFoundException("존재하지 않는 친구관계 입니다.");
        });

        friendRepository.delete(friend);
        return new FriendDTO.FriendResponse(friend);
    }

    @Override
    @Transactional
    public FriendDTO.FriendResponse updateFriendName(Users me, Long mateId, FriendDTO.UpdateFriendNameRequest updateFriendNameRequest) {
        Long myId = me.getId();
        Friend friend = friendRepository.findByMeIdAndMateId(myId, mateId).orElseThrow(() -> {
            throw new EntityNotFoundException("존재하지 않는 친구 관계 입니다.");
        });
        String updateFriendName = updateFriendNameRequest.getFriendName();
        friend.setFriendName(updateFriendName);

        return new FriendDTO.FriendResponse(friend);
    }
}

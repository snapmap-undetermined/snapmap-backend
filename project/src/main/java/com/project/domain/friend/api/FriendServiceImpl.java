package com.project.domain.friend.api;

import com.project.common.exception.BusinessLogicException;
import com.project.common.exception.EntityNotFoundException;
import com.project.common.exception.ErrorCode;
import com.project.common.exception.InvalidValueException;
import com.project.domain.friend.dto.FriendDTO;
import com.project.domain.friend.entity.Friend;
import com.project.domain.friend.repository.FriendRepository;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Override
    public FriendDTO.FriendListResponse getAllFriends(Long userId) {
        List<FriendDTO.FriendResponse> friendList = friendRepository.findAllFriendsOfUser(userId);
        log.info("Get All friends by userId : {}, {}", userId, friendList);
        return new FriendDTO.FriendListResponse(friendList);
    }

    @Override
    @Transactional
    public FriendDTO.FriendResponse createFriend(Users user, FriendDTO.CreateFriendRequest createFriendRequest) {
        Users mate = userRepository.findById(createFriendRequest.getFriendUserId()).orElseThrow(() -> {
            log.error("Target friend does not exist. userId : {}", createFriendRequest.getFriendUserId());
            throw new EntityNotFoundException("User does not exists.");
        });

        if (Objects.equals(user.getId(), mate.getId())) {
            log.error("Cannot make friends with yourself. myId : {}, mateId: {}", user.getId(), mate.getId());
            throw new InvalidValueException("Cannot make friends with yourself.", ErrorCode.INVALID_INPUT_VALUE);
        }

        if (friendRepository.existsByUserIds(user.getId(), mate.getId())) {
            log.error("Already exists friendship. myId : {}, mateId: {}", user.getId(), mate.getId());
            throw new BusinessLogicException("Already exists friendship.",ErrorCode.FRIEND_DUPLICATION);
        }

        Friend friend = Friend.builder().me(user).mate(mate).friendName(mate.getNickname()).build();
        friendRepository.save(friend);
        log.info("Created friend. myId : {}, mateId : {}", user.getId(), friend.getId());
        return new FriendDTO.FriendResponse(friend);
    }

    @Override
    @Transactional
    public FriendDTO.FriendResponse deleteFriend(Users me, Long mateId) {
        Long myId = me.getId();
        Friend friend = getFriend(myId, mateId);

        friendRepository.delete(friend);
        log.info("Delete friend, myId : {}, mateId : {}", me.getId(), mateId);
        return new FriendDTO.FriendResponse(friend);
    }

    @Override
    @Transactional
    public FriendDTO.FriendResponse updateFriendName(Users me, Long mateId, FriendDTO.UpdateFriendNameRequest updateFriendNameRequest) {
        Long myId = me.getId();
        Friend friend = getFriend(myId, mateId);

        String updateFriendName = updateFriendNameRequest.getFriendName();
        if (updateFriendName != null) {
            log.info("Update friend name : {} -> {}", friend.getFriendName(), updateFriendName);
            friend.setFriendName(updateFriendName);
        }

        return new FriendDTO.FriendResponse(friend);
    }

    @Transactional
    public Friend getFriend(Long myId, Long mateId) {
        Friend friend = friendRepository.findByMeIdAndMateId(myId, mateId).orElse(null);
        if (friend == null) {
            log.error("Friend does not exist. myId : {}, friendId : {}", myId, mateId);
            throw new EntityNotFoundException("Friend does not exist.");
        }

        return friend;
    }
}

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
@Slf4j
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
    public FriendDTO.FriendResponse createFriend(Users me, FriendDTO.CreateFriendRequest createFriendRequest) {
        Users mate = userRepository.findById(createFriendRequest.getFriendUserId()).orElseThrow(() -> {
            throw new EntityNotFoundException("User does not exists.", ErrorCode.ENTITY_NOT_FOUND);
        });

        if (Objects.equals(me.getId(), mate.getId())) {
            log.error("Mate Id : {}", mate.getId());
            throw new InvalidValueException("Cannot make friends with yourself.", ErrorCode.INVALID_INPUT_VALUE);
        }

        if (friendRepository.existsByUserIds(me.getId(), mate.getId())) {
            throw new BusinessLogicException("Already exists friendship.",ErrorCode.FRIEND_DUPLICATION);
        }

        Friend friend = Friend.builder().me(me).mate(mate).friendName(mate.getNickname()).build();
        friendRepository.save(friend);
        return new FriendDTO.FriendResponse(friend);
    }

    @Override
    @Transactional
    public FriendDTO.FriendResponse deleteFriend(Users me, Long mateId) {
        Long myId = me.getId();
        Friend friend = getFriend(myId, mateId);
        friendRepository.delete(friend);
        return new FriendDTO.FriendResponse(friend);
    }

    @Override
    @Transactional
    public FriendDTO.FriendResponse updateFriendName(Users me, Long mateId, FriendDTO.UpdateFriendNameRequest updateFriendNameRequest) {
        Long myId = me.getId();
        Friend friend = getFriend(myId, mateId);
        String updateFriendName = updateFriendNameRequest.getFriendName();
        friend.setFriendName(updateFriendName);

        return new FriendDTO.FriendResponse(friend);
    }

    @Transactional
    public Friend getFriend(Long myId, Long mateId) {
        return friendRepository.findByMeIdAndMateId(myId, mateId).orElseThrow(() -> {
            throw new EntityNotFoundException("Friend does not exists.");
        });
    }
}

package com.project.domain.friend.api;

import com.project.domain.friend.dto.FriendDTO;
import com.project.domain.friend.entity.Friend;
import com.project.domain.friend.repository.FriendRepository;
import com.project.domain.users.entity.Users;
import com.project.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Override
    public List<FriendDTO.FriendSimpleInfoResponse> getAllFriends(Long userId) throws Exception {

        List<Friend> friendList = friendRepository.findByUserId(userId);

        return friendList.stream().map(FriendDTO.FriendSimpleInfoResponse::new).collect(Collectors.toList());

    }

    @Override
    public FriendDTO.FriendSimpleInfoResponse createFriend(Users user, FriendDTO.CreateFriendRequest createFriendRequest) throws Exception {

        Friend friend = createFriendRequest.toEntity();
        Users friendUser = userRepository.findById(createFriendRequest.getFriendUserId()).orElseThrow();

        friend.setFriendUser(friendUser);
        friend.setMeUser(user);

        friendRepository.save(friend);

        return new FriendDTO.FriendSimpleInfoResponse(friend);
    }

    @Override
    public void deleteFriend(Long friendId) throws Exception {

        Friend friend = friendRepository.findById(friendId).orElseThrow(() -> new NoSuchElementException("친구관계에 존재하지 않습니다."));
        friendRepository.delete(friend);

    }

    @Override
    public FriendDTO.FriendSimpleInfoResponse updateFriendName(Long friendId, FriendDTO.UpdateFriendNameRequest updateFriendNameRequest) throws Exception {
        Friend friend = friendRepository.findById(friendId).orElseThrow();
        String updateFriendName = updateFriendNameRequest.getFriendName();
        friend.setFriendName(updateFriendName);

        return new FriendDTO.FriendSimpleInfoResponse(friend);
    }
}

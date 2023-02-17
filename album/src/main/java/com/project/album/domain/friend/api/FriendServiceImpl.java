package com.project.album.domain.friend.api;

import com.project.album.domain.friend.dto.FriendDTO;
import com.project.album.domain.friend.entity.Friend;
import com.project.album.domain.friend.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService{

    private final FriendRepository friendRepository;

    @Override
    public List<FriendDTO.GetFriendListResponse> getFriendListByUser(Long userId) throws Exception {

        List<Friend> friendList = friendRepository.findByUserId(userId);

        return friendList.stream().map(FriendDTO.GetFriendListResponse::new).collect(Collectors.toList());

    }

    @Override
    public FriendDTO.FriendDetailResponse createFriend(Long userId, FriendDTO.CreateFriendRequest createFriendRequest) throws Exception {

        Friend friend = createFriendRequest.toEntity();
        friendRepository.save(friend);

        return new FriendDTO.FriendDetailResponse(friend);
    }

    @Override
    public void deleteFriend(Long friendId) throws Exception {

        Friend friend = friendRepository.findById(friendId).orElseThrow(() -> new NoSuchElementException("친구관계에 존재하지 않습니다."));
        friendRepository.delete(friend);

    }

    @Override
    public FriendDTO.UpdateFriendNameResponse updateFriendName(Long friendId, FriendDTO.UpdateFriendNameRequest updateFriendNameRequest) throws Exception {
        Friend friend = friendRepository.findById(friendId).orElseThrow();
        String updateFriendName = updateFriendNameRequest.getFriendName();
        friend.setFriendName(updateFriendName);

        return new FriendDTO.UpdateFriendNameResponse(friend);
    }
}

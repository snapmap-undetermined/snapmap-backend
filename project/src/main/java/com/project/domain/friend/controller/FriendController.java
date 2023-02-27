package com.project.domain.friend.controller;

import com.project.common.annotation.AuthUser;
import com.project.domain.friend.api.FriendService;
import com.project.domain.friend.dto.FriendDTO;
import com.project.domain.users.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/friend")
public class FriendController {

    private final FriendService friendService;

    // user별 친구목록 조회
    @GetMapping("")
    private ResponseEntity<List<FriendDTO.FriendSimpleInfoResponse>> getFriendListByUser(@AuthUser Users user) throws Exception {
        List<FriendDTO.FriendSimpleInfoResponse> response = friendService.getAllFriends(user.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 검색해서 (파라미터로) 친구목록에 생성
    @PostMapping("")
    private ResponseEntity<FriendDTO.FriendSimpleInfoResponse> createFriend(@AuthUser Users user, @RequestBody FriendDTO.CreateFriendRequest request) throws Exception {
        FriendDTO.FriendSimpleInfoResponse response = friendService.createFriend(user, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 친구삭제
    @DeleteMapping("/{friendId}")
    private ResponseEntity<FriendDTO.FriendSimpleInfoResponse> deleteFriend(@PathVariable Long friendId) throws Exception {
        friendService.deleteFriend(friendId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 친구이름 수정
    @PatchMapping("/{friendId}")
    private ResponseEntity<FriendDTO.FriendSimpleInfoResponse> updateFriendName(@PathVariable Long friendId, @RequestBody FriendDTO.UpdateFriendNameRequest updateFriendNameRequest) throws Exception {
        FriendDTO.FriendSimpleInfoResponse response = friendService.updateFriendName(friendId, updateFriendNameRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

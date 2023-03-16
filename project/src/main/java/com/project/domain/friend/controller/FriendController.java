package com.project.domain.friend.controller;

import com.project.common.annotation.AuthUser;
import com.project.common.exception.EntityNotFoundException;
import com.project.common.exception.ErrorCode;
import com.project.domain.circle.dto.CircleDTO;
import com.project.domain.circle.entity.Circle;
import com.project.domain.friend.api.FriendService;
import com.project.domain.friend.dto.FriendDTO;
import com.project.domain.usercircle.entity.UserCircle;
import com.project.domain.users.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/friend")
public class FriendController {

    private final FriendService friendService;

    // user별 친구목록 조회
    @GetMapping("")
    private ResponseEntity<FriendDTO.FriendListResponse> getFriendListByUser(@AuthUser Users user) throws Exception {

        FriendDTO.FriendListResponse response = friendService.getAllFriends(user.getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 검색해서 (파라미터로) 친구목록에 생성
    @PostMapping("")
    private ResponseEntity<FriendDTO.FriendResponse> createFriend(@AuthUser Users user, @RequestBody FriendDTO.CreateFriendRequest request) throws Exception {
        FriendDTO.FriendResponse response = friendService.createFriend(user, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 친구삭제
    @DeleteMapping("/{friendId}")
    private ResponseEntity<FriendDTO.FriendResponse> deleteFriend(@PathVariable Long friendId) throws Exception {
        friendService.deleteFriend(friendId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 친구이름 수정
    @PatchMapping("/{friendId}")
    private ResponseEntity<FriendDTO.FriendResponse> updateFriendName(@PathVariable Long friendId, @RequestBody FriendDTO.UpdateFriendNameRequest updateFriendNameRequest) throws Exception {
        FriendDTO.FriendResponse response = friendService.updateFriendName(friendId, updateFriendNameRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

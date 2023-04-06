package com.project.domain.friend.controller;

import com.project.common.annotation.AuthUser;
import com.project.common.annotation.Permission;
import com.project.domain.friend.api.FriendService;
import com.project.domain.friend.dto.FriendDTO;
import com.project.domain.users.entity.Users;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "friend",description = "유저 친구 기능 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/friend")
public class FriendController {

    private final FriendService friendService;

    // user별 친구목록 조회
    @GetMapping("")
    @Permission
    private ResponseEntity<FriendDTO.FriendListResponse> getFriendListByUser(@AuthUser Users user) throws Exception {

        FriendDTO.FriendListResponse response = friendService.getAllFriends(user.getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 검색해서 (파라미터로) 친구목록에 생성
    @PostMapping("")
    @Permission
    private ResponseEntity<FriendDTO.FriendResponse> createFriend(@AuthUser Users user, @RequestBody FriendDTO.CreateFriendRequest request) throws Exception {
        FriendDTO.FriendResponse response = friendService.createFriend(user, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 친구삭제
    @PatchMapping("/{mateId}")
    @Permission
    private ResponseEntity<FriendDTO.FriendResponse> deleteFriend(@AuthUser Users user, @PathVariable Long mateId) throws Exception {
        FriendDTO.FriendResponse response = friendService.deleteFriend(user, mateId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 친구이름 수정
    @PatchMapping("/{mateId}/name")
    @Permission
    private ResponseEntity<FriendDTO.FriendResponse> updateFriendName(@AuthUser Users user, @PathVariable Long mateId, @RequestBody FriendDTO.UpdateFriendNameRequest updateFriendNameRequest) throws Exception {
        FriendDTO.FriendResponse response = friendService.updateFriendName(user, mateId, updateFriendNameRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

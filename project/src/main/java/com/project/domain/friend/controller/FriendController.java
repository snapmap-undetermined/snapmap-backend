package com.project.domain.friend.controller;

import com.project.common.annotation.AuthUser;
import com.project.common.annotation.Permission;
import com.project.domain.friend.api.FriendService;
import com.project.domain.friend.dto.FriendDTO;
import com.project.domain.pin.dto.PinDTO;
import com.project.domain.users.entity.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@Tag(name = "친구 API", description = "Friend Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/friend")
public class FriendController {

    private final FriendService friendService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = FriendDTO.FriendListResponse.class)))})
    @Operation(summary = "친구 리스트 조회", description = "자신의 친구 리스트를 조회 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("")
    @Permission
    private ResponseEntity<FriendDTO.FriendListResponse> getFriendListByUser(@AuthUser Users user) throws Exception {

        FriendDTO.FriendListResponse response = friendService.getAllFriends(user.getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = FriendDTO.FriendResponse.class)))})
    @Operation(summary = "친구 추가", description = "친구를 추가 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("")
    @Permission
    private ResponseEntity<FriendDTO.FriendResponse> createFriend(@AuthUser Users user, @RequestBody FriendDTO.CreateFriendRequest request) throws Exception {
        FriendDTO.FriendResponse response = friendService.createFriend(user, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = FriendDTO.FriendResponse.class)))})
    @Operation(summary = "친구 삭제", description = "친구를 삭제 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{mateId}")
    @Permission
    private ResponseEntity<FriendDTO.FriendResponse> deleteFriend(@AuthUser Users user, @Parameter(description = "친구의 id") @PathVariable Long mateId) throws Exception {
        FriendDTO.FriendResponse response = friendService.deleteFriend(user, mateId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = FriendDTO.FriendResponse.class)))})
    @Operation(summary = "친구 이름 수정", description = "친구 이름을 수정한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{mateId}/name")
    @Permission
    private ResponseEntity<FriendDTO.FriendResponse> updateFriendName(@AuthUser Users user, @Parameter(description = "친구의 id") @PathVariable Long mateId, @RequestBody FriendDTO.UpdateFriendNameRequest updateFriendNameRequest) throws Exception {
        FriendDTO.FriendResponse response = friendService.updateFriendName(user, mateId, updateFriendNameRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

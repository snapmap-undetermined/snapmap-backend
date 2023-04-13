package com.project.domain.comment.controller;

import com.project.common.annotation.AuthUser;
import com.project.common.annotation.Permission;
import com.project.domain.comment.api.PinCommentService;
import com.project.domain.comment.dto.PictureCommentDTO;
import com.project.domain.comment.dto.PinCommentDTO;
import com.project.domain.friend.dto.FriendDTO;
import com.project.domain.pin.api.PinService;
import com.project.domain.users.dto.UserDTO;
import com.project.domain.users.entity.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "핀 댓글 API", description = "Pin Comment Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/pin-comment")
public class PinCommentController {

    private final PinCommentService pinCommentService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "핀 댓글 생성 성공", content = @Content(schema = @Schema(implementation = PinCommentDTO.PinCommentDetailResponse.class)))})
    @Operation(summary = "핀 댓글 생성", description = "핀 댓글을 생성한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("")
    @Permission
    public ResponseEntity<PinCommentDTO.PinCommentDetailResponse> createPinComment(@AuthUser Users user, @RequestBody PinCommentDTO.CreatePinCommentRequest request) {
        PinCommentDTO.PinCommentDetailResponse response = pinCommentService.createPinComment(user, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "핀에 속한 핀 댓글 리스트 조회 성공", content = @Content(schema = @Schema(implementation = PinCommentDTO.PinCommentListResponse.class)))})
    @Operation(summary = "핀 댓글 리스트 조회", description = "핀에 속한 핀 댓글 리스트를 조회 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{pinId}")
    @Permission
    public ResponseEntity<PinCommentDTO.PinCommentListResponse> getPinCommentByPinId(@PathVariable Long pinId) {
        List<PinCommentDTO.PinCommentDetailResponse> pinCommentDetailResponseList = pinCommentService.getPinCommentByPinId(pinId);

        PinCommentDTO.PinCommentListResponse response = new PinCommentDTO.PinCommentListResponse(pinCommentDetailResponseList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "핀 댓글 삭제 성공", content = @Content(schema = @Schema(implementation = PinCommentDTO.PinCommentDetailResponse.class)))})
    @Operation(summary = "핀 댓글 삭제", description = "핀 댓글을 삭제 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{pinCommentId}")
    @Permission
    public ResponseEntity<PinCommentDTO.PinCommentDetailResponse> deletePinComment(@PathVariable Long pinCommentId) {
        pinCommentService.deletePinComment(pinCommentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "핀 댓글 상태 삭제 성공", content = @Content(schema = @Schema(implementation = PinCommentDTO.PinCommentDetailResponse.class)))})
    @Operation(summary = "핀 댓글 상태 삭제", description = "핀 댓글 상태 삭제를 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/status/{pinCommentId}")
    @Permission
    public ResponseEntity<PinCommentDTO.PinCommentDetailResponse> deletePinCommentWithStatus(@PathVariable Long pinCommentId) {
        pinCommentService.deletePinCommentWithStatus(pinCommentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "핀 댓글 수정 성공", content = @Content(schema = @Schema(implementation = PinCommentDTO.PinCommentDetailResponse.class)))})
    @Operation(summary = "핀 댓글 수정", description = "핀 댓글을 수정한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{pinCommentId}")
    @Permission
    public ResponseEntity<PinCommentDTO.PinCommentDetailResponse> updatePinComment(@PathVariable Long pinCommentId, @RequestBody PinCommentDTO.UpdatePinCommentRequest request) {
        PinCommentDTO.PinCommentDetailResponse response = pinCommentService.updatePinComment(pinCommentId, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

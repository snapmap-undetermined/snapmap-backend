package com.project.domain.comment.controller;

import com.project.common.annotation.AuthUser;
import com.project.common.annotation.Permission;
import com.project.domain.comment.api.PictureCommentService;
import com.project.domain.comment.dto.PictureCommentDTO;
import com.project.domain.users.dto.UserDTO;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "사진 댓글 API", description = "Picture Comment Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/picture-comment")
public class PictureCommentController {

    private final PictureCommentService pictureCommentService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PictureCommentDTO.PictureCommentDetailResponse.class)))})
    @Operation(summary = "사진 댓글 생성", description = "사진 댓글을 생성한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("")
    @Permission
    public ResponseEntity<PictureCommentDTO.PictureCommentDetailResponse> createPictureComment(@AuthUser Users user, @RequestBody PictureCommentDTO.CreatePictureCommentRequest request) {
        PictureCommentDTO.PictureCommentDetailResponse response = pictureCommentService.createPictureComment(user, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PictureCommentDTO.PictureCommentListResponse.class)))})
    @Operation(summary = "사진 댓글 리스트 조회", description = "사진에 속한 사진 댓글 리스트를 조회 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{pictureId}")
    @Permission
    public ResponseEntity<PictureCommentDTO.PictureCommentListResponse> getPictureCommentByPictureId(@Parameter(description = "사진의 id") @PathVariable Long pictureId) {
        List<PictureCommentDTO.PictureCommentDetailResponse> pictureCommentDetailResponseList = pictureCommentService.getPictureCommentByPictureId(pictureId);

        PictureCommentDTO.PictureCommentListResponse response = new PictureCommentDTO.PictureCommentListResponse(pictureCommentDetailResponseList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PictureCommentDTO.PictureCommentDetailResponse.class)))})
    @Operation(summary = "사진 댓글 삭제", description = "사진 댓글을 삭제 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{pictureCommentId}")
    @Permission
    public ResponseEntity<PictureCommentDTO.PictureCommentDetailResponse> deletePictureComment(@Parameter(description = "사진 댓글의 id") @PathVariable Long pictureCommentId) {
        pictureCommentService.deletePictureComment(pictureCommentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PictureCommentDTO.PictureCommentDetailResponse.class)))})
    @Operation(summary = "사진 댓글 상태 삭제", description = "사진 댓글 상태 삭제를 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/status/{pictureCommentId}")
    @Permission
    public ResponseEntity<PictureCommentDTO.PictureCommentDetailResponse> deletePictureCommentWithStatus(@Parameter(description = "사진 댓글의 id") @PathVariable Long pictureCommentId) {
        pictureCommentService.deletePictureCommentWithStatus(pictureCommentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserDTO.UserSimpleInfoResponse.class)))})
    @Operation(summary = "사진 댓글 수정", description = "사진 댓글을 수정한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{pictureCommentId}")
    @Permission
    public ResponseEntity<PictureCommentDTO.PictureCommentDetailResponse> updatePictureComment(@Parameter(description = "사진 댓글의 id") @PathVariable Long pictureCommentId, @RequestBody PictureCommentDTO.UpdatePictureCommentRequest request) {
        PictureCommentDTO.PictureCommentDetailResponse response = pictureCommentService.updatePictureComment(pictureCommentId, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

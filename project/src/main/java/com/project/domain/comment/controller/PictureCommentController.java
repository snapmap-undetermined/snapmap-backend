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

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PictureCommentDTO.PictureCommentDetailResponse.class)))})
    @Operation(summary = "사진 댓글 생성", description = "사진 댓글을 생성한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/picture/{pictureId}")
    @Permission
    public ResponseEntity<PictureCommentDTO.PictureCommentDetailResponse> createPictureComment(@AuthUser Users user, @Parameter(description = "사진의 ID") @PathVariable Long pictureId, @RequestBody PictureCommentDTO.CreatePictureCommentRequest request) {
        PictureCommentDTO.PictureCommentDetailResponse response = pictureCommentService.createPictureComment(user, pictureId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PictureCommentDTO.PictureCommentListResponse.class)))})
    @Operation(summary = "사진 댓글 리스트 조회", description = "사진에 속한 댓글 리스트를 조회한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/picture/{pictureId}/all")
    @Permission
    public ResponseEntity<PictureCommentDTO.PictureCommentListResponse> getPictureCommentByPictureId(@AuthUser Users user, @Parameter(description = "사진의 ID") @PathVariable Long pictureId) {
        List<PictureCommentDTO.PictureCommentDetailResponse> pictureCommentDetailResponseList = pictureCommentService.getPictureCommentByPictureId(pictureId);
        PictureCommentDTO.PictureCommentListResponse response = new PictureCommentDTO.PictureCommentListResponse(pictureCommentDetailResponseList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PictureCommentDTO.PictureCommentDetailResponse.class)))})
    @Operation(summary = "사진 댓글 삭제", description = "자신이 작성한 댓글 중 최상위 댓글이 아닐 경구, 영구 삭제한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{pictureCommentId}")
    @Permission
    public ResponseEntity<PictureCommentDTO.PictureCommentDetailResponse> deletePictureComment(@AuthUser Users user, @Parameter(description = "사진 댓글의 ID") @PathVariable Long pictureCommentId) {
        pictureCommentService.deletePictureComment(pictureCommentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PictureCommentDTO.PictureCommentDetailResponse.class)))})
    @Operation(summary = "상태 기반 사진 댓글 삭제", description = "자신이 작성한 댓글 중 최상위 댓글일 경구, 자식 댓글의 보존을 위해 댓글의 상태값을 삭제 상태로 수정한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{pictureCommentId}/status")
    @Permission
    public ResponseEntity<PictureCommentDTO.PictureCommentDetailResponse> deletePictureCommentWithStatus(@AuthUser Users user, @Parameter(description = "사진 댓글의 ID") @PathVariable Long pictureCommentId) {
        pictureCommentService.deletePictureCommentWithStatus(pictureCommentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserDTO.UserSimpleInfoResponse.class)))})
    @Operation(summary = "사진 댓글 수정", description = "자신이 작성한 특정 댓글을 수정한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{pictureCommentId}")
    @Permission
    public ResponseEntity<PictureCommentDTO.PictureCommentDetailResponse> updatePictureComment(@AuthUser Users user, @Parameter(description = "사진 댓글의 ID") @PathVariable Long pictureCommentId, @RequestBody PictureCommentDTO.UpdatePictureCommentRequest request) {
        PictureCommentDTO.PictureCommentDetailResponse response = pictureCommentService.updatePictureComment(pictureCommentId, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

package com.project.domain.comment.controller;

import com.project.common.annotation.AuthUser;
import com.project.common.annotation.Permission;
import com.project.domain.comment.api.PictureCommentService;
import com.project.domain.comment.dto.PictureCommentDTO;
import com.project.domain.users.entity.Users;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "picture-comment",description = "사진 댓글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/picture-comment")
public class PictureCommentController {

    private final PictureCommentService pictureCommentService;

    @PostMapping("")
    @Permission
    public ResponseEntity<PictureCommentDTO.PictureCommentDetailResponse> createPictureComment(@AuthUser Users user, @RequestBody PictureCommentDTO.CreatePictureCommentRequest request) {
        PictureCommentDTO.PictureCommentDetailResponse response = pictureCommentService.createPictureComment(user, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{pictureId}")
    @Permission
    public ResponseEntity<PictureCommentDTO.PictureCommentListResponse> getPictureCommentByPictureId(@PathVariable Long pictureId) {
        List<PictureCommentDTO.PictureCommentDetailResponse> pictureCommentDetailResponseList = pictureCommentService.getPictureCommentByPictureId(pictureId);

        PictureCommentDTO.PictureCommentListResponse response = new PictureCommentDTO.PictureCommentListResponse(pictureCommentDetailResponseList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{pictureCommentId}")
    @Permission
    public ResponseEntity<PictureCommentDTO.PictureCommentDetailResponse> deletePictureComment(@PathVariable Long pictureCommentId) {
        pictureCommentService.deletePictureComment(pictureCommentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/status/{pictureCommentId}")
    @Permission
    public ResponseEntity<PictureCommentDTO.PictureCommentDetailResponse> deletePictureCommentWithStatus(@PathVariable Long pictureCommentId) {
        pictureCommentService.deletePictureCommentWithStatus(pictureCommentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{pictureCommentId}")
    @Permission
    public ResponseEntity<PictureCommentDTO.PictureCommentDetailResponse> updatePictureComment(@PathVariable Long pictureCommentId, @RequestBody PictureCommentDTO.UpdatePictureCommentRequest request) {
        PictureCommentDTO.PictureCommentDetailResponse response = pictureCommentService.updatePictureComment(pictureCommentId, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

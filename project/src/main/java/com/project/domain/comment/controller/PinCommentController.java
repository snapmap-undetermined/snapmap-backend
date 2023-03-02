package com.project.domain.comment.controller;

import com.project.common.annotation.AuthUser;
import com.project.domain.comment.api.PinCommentService;
import com.project.domain.comment.dto.PictureCommentDTO;
import com.project.domain.comment.dto.PinCommentDTO;
import com.project.domain.friend.dto.FriendDTO;
import com.project.domain.pin.api.PinService;
import com.project.domain.users.entity.Users;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/pin-comment")
public class PinCommentController {

    private final PinCommentService pinCommentService;

    @PostMapping("")
    public ResponseEntity<PinCommentDTO.PinCommentDetailResponse> createPinComment(@AuthUser Users user, @RequestBody PinCommentDTO.CreatePinCommentRequest request) {
        PinCommentDTO.PinCommentDetailResponse response = pinCommentService.createPinComment(user, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{pinId}")
    public ResponseEntity<PinCommentDTO.PinCommentListResponse> getPinCommentByPinId(@PathVariable Long pinId) {
        List<PinCommentDTO.PinCommentDetailResponse> pinCommentDetailResponseList = pinCommentService.getPinCommentByPinId(pinId);

        PinCommentDTO.PinCommentListResponse response = new PinCommentDTO.PinCommentListResponse(pinCommentDetailResponseList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{pinCommentId}")
    public ResponseEntity<PinCommentDTO.PinCommentDetailResponse> deletePinComment(@PathVariable Long pinCommentId) {
        pinCommentService.deletePinComment(pinCommentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/status/{pinCommentId}")
    public ResponseEntity<PinCommentDTO.PinCommentDetailResponse> deletePinCommentWithStatus(@PathVariable Long pinCommentId) {
        pinCommentService.deletePinCommentWithStatus(pinCommentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{pinCommentId}")
    public ResponseEntity<PinCommentDTO.PinCommentDetailResponse> updatePinComment(@PathVariable Long pinCommentId, @RequestBody PinCommentDTO.UpdatePinCommentRequest request) {
        PinCommentDTO.PinCommentDetailResponse response = pinCommentService.updatePinComment(pinCommentId, request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}

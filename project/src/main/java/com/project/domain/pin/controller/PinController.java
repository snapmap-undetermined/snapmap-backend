package com.project.domain.pin.controller;

import com.project.common.annotation.AuthUser;
import com.project.common.annotation.Permission;
import com.project.domain.picture.dto.PictureDTO;
import com.project.domain.pin.api.PinService;
import com.project.domain.pin.dto.PinDTO;
import com.project.domain.users.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/pin")
public class PinController {

    private final PinService pinService;


    @PostMapping("/{circleId}")
    @Permission
    public ResponseEntity<PinDTO.PinDetailResponse> createPin(@AuthUser Users user, @PathVariable Long circleId,
                                                              @RequestPart PinDTO.PinCreateRequest request,
                                                              @RequestPart List<MultipartFile> pictures) {

        PinDTO.PinDetailResponse pin = pinService.createPin(user, circleId, request, pictures);
        return new ResponseEntity<>(pin, HttpStatus.OK);
    }

    @GetMapping("/circle/{circleId}")
    @Permission
    public ResponseEntity<PinDTO.PinDetailListResponse> getAllPinByCircle(@AuthUser Users user, @PathVariable Long circleId) {
        PinDTO.PinDetailListResponse pinList = pinService.getAllPinsByCircle(circleId);
        return new ResponseEntity<>(pinList, HttpStatus.OK);
    }

    @GetMapping("/{pinId}")
    @Permission
    public ResponseEntity<?> getPin(@AuthUser Users user, @PathVariable Long pinId) {
        PinDTO.PinDetailResponse pin = pinService.getPinDetail(user, pinId);
        return new ResponseEntity<>(pin, HttpStatus.OK);
    }

    @GetMapping("/me")
    @Permission
    public ResponseEntity<PinDTO.PinDetailListResponse> getAllPinByMe(@AuthUser Users user) {
        PinDTO.PinDetailListResponse pinList = pinService.getAllPinByMe(user);
        return new ResponseEntity<>(pinList, HttpStatus.OK);
    }

    @PatchMapping("/{pinId}")
    @Permission
    public ResponseEntity<PinDTO.PinDetailResponse> updatePin(@AuthUser Users user, @PathVariable Long pinId,
                                                              @RequestPart PinDTO.PinUpdateRequest request,
                                                              @RequestPart List<MultipartFile> pictures) throws Exception{
        PinDTO.PinDetailResponse pin = pinService.updatePin(user, pinId, request, pictures);
        return new ResponseEntity<>(pin, HttpStatus.OK);
    }

    @PostMapping("/{pinId}/inactive")
    @Permission
    public ResponseEntity<Long> deletePin(@AuthUser Users user, @PathVariable Long pinId) {
        pinService.deletePin(user, pinId);
        return new ResponseEntity<>(pinId, HttpStatus.OK);
    }

    @GetMapping("/{pinId}/picture/{pictureId}")
    @Permission
    public ResponseEntity<PinDTO.PinWithDistinctPictureResponse> getPictureDetail(@AuthUser Users user, @PathVariable Long pictureId, @PathVariable Long pinId) {

        PinDTO.PinWithDistinctPictureResponse response = pinService.getPictureDetail(user, pictureId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

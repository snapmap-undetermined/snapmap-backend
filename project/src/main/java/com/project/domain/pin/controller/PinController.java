package com.project.domain.pin.controller;

import com.project.common.annotation.AuthUser;
import com.project.common.annotation.Permission;
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
        List<PinDTO.PinDetailResponse> pinList = pinService.getAllPinByCircle(circleId);
        PinDTO.PinDetailListResponse pinDetailListResponse = new PinDTO.PinDetailListResponse(pinList);
        return new ResponseEntity<>(pinDetailListResponse, HttpStatus.OK);
    }

    @GetMapping("/{pinId}")
    @Permission
    public ResponseEntity<PinDTO.PinDetailResponse> getPin(@AuthUser Users user, @PathVariable Long pinId) {
        PinDTO.PinDetailResponse pin = pinService.getPinDetail(user, pinId);
        return new ResponseEntity<>(pin, HttpStatus.OK);
    }

    @GetMapping("/me")
    @Permission
    public ResponseEntity<PinDTO.PinDetailListResponse> getAllPinByMe(@AuthUser Users user) {
        List<PinDTO.PinDetailResponse> pinList = pinService.getAllPinByMe(user);
        PinDTO.PinDetailListResponse pinDetailListResponse = new PinDTO.PinDetailListResponse(pinList);
        return new ResponseEntity<>(pinDetailListResponse, HttpStatus.OK);
    }

    @PatchMapping("/")
    @Permission
    public ResponseEntity<PinDTO.PinDetailResponse> updatePin(@AuthUser Users user, @RequestBody PinDTO.PinUpdateRequest request) throws Exception{
        PinDTO.PinDetailResponse pin = pinService.updatePin(user, request);
        return new ResponseEntity<>(pin, HttpStatus.OK);
    }

    @DeleteMapping("/{pinId}")
    @Permission
    public ResponseEntity<Long> deletePin(@AuthUser Users user, @PathVariable Long pinId) {
        pinService.deletePin(user, pinId);
        return new ResponseEntity<>(pinId, HttpStatus.OK);
    }
}

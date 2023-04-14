package com.project.domain.pin.controller;

import com.project.common.annotation.AuthUser;
import com.project.common.annotation.Permission;
import com.project.domain.pin.api.PinService;
import com.project.domain.pin.dto.PinDTO;
import com.project.domain.users.entity.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "핀 API", description = "Pin Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/pin")
public class PinController {

    private final PinService pinService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PinDTO.PinDetailResponse.class)))})
    @Operation(summary = "핀 생성", description = "핀을 생성한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/pocket/{pocketId}")
    @Permission
    public ResponseEntity<PinDTO.PinDetailResponse> createPin(@AuthUser Users user,
                                                              @Parameter(description = "포켓의 id")
                                                              @PathVariable Long pocketId,
                                                              @RequestPart PinDTO.PinCreateRequest request,
                                                              @RequestPart List<MultipartFile> pictures) {

        PinDTO.PinDetailResponse pin = pinService.createPin(user, pocketId, request, pictures);
        return new ResponseEntity<>(pin, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PinDTO.PinDetailListResponse.class)))})
    @Operation(summary = "포켓에 속한 핀 전체 조회", description = "포켓에 속한 핀을 모두 조회한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/pocket/{pocketId}")
    @Permission
    public ResponseEntity<PinDTO.PinDetailListResponse> getAllPinByPocket(@AuthUser Users user, @Parameter(description = "포켓의 id") @PathVariable Long pocketId) {
        PinDTO.PinDetailListResponse pinList = pinService.getAllPinsByPocket(pocketId);
        return new ResponseEntity<>(pinList, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PinDTO.PinDetailResponse.class)))})
    @Operation(summary = "핀 상세정보 조회", description = "핀의 상세정보를 조회 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{pinId}")
    @Permission
    public ResponseEntity<?> getPin(@AuthUser Users user, @Parameter(description = "핀의 id") @PathVariable Long pinId) {
        PinDTO.PinDetailResponse pin = pinService.getPinDetail(user, pinId);
        return new ResponseEntity<>(pin, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PinDTO.PinDetailResponse.class)))})
    @Operation(summary = "자신이 가진 핀 전체 조회", description = "자신이 가지고 있는 핀 전제를 조회 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/me")
    @Permission
    public ResponseEntity<PinDTO.PinDetailListResponse> getAllPinByMe(@AuthUser Users user) {
        PinDTO.PinDetailListResponse pinList = pinService.getAllPinByMe(user);
        return new ResponseEntity<>(pinList, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PinDTO.PinDetailResponse.class)))})
    @Operation(summary = "핀 수정", description = "핀을 수정한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{pinId}")
    @Permission
    public ResponseEntity<PinDTO.PinDetailResponse> updatePin(@AuthUser Users user,
                                                              @Parameter(description = "핀의 id") @PathVariable Long pinId,
                                                              @RequestPart PinDTO.PinUpdateRequest request,
                                                              @RequestPart List<MultipartFile> pictures) throws Exception {
        PinDTO.PinDetailResponse pin = pinService.updatePin(user, pinId, request, pictures);
        return new ResponseEntity<>(pin, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Long.class)))})
    @Operation(summary = "핀 삭제", description = "핀을 삭제한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/{pinId}/inactive")
    @Permission
    public ResponseEntity<Long> deletePin(@AuthUser Users user, @Parameter(description = "핀의 id") @PathVariable Long pinId) {
        pinService.deletePin(user, pinId);
        return new ResponseEntity<>(pinId, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PinDTO.PinWithDistinctPictureResponse.class)))})
    @Operation(summary = "사진 상세정보 조회", description = "사진 상세정보를 조회 한다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{pinId}/picture/{pictureId}")
    @Permission
    public ResponseEntity<PinDTO.PinWithDistinctPictureResponse> getPictureDetail(
            @AuthUser Users user,
            @Parameter(description = "사진의 id") @PathVariable Long pictureId,
            @Parameter(description = "핀의 id") @PathVariable Long pinId) {

        PinDTO.PinWithDistinctPictureResponse response = pinService.getPictureDetail(user, pictureId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

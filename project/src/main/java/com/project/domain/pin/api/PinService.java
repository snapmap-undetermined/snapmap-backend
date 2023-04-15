package com.project.domain.pin.api;

import com.project.domain.pin.dto.PinDTO;
import com.project.domain.users.entity.Users;
import org.locationtech.jts.io.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PinService {
    PinDTO.PinDetailResponse createPin(Users user, Long pocketId, PinDTO.PinCreateRequest request, List<MultipartFile> pictures);

    PinDTO.PinDetailResponse getPinDetail(Users user, Long pinId);

    PinDTO.PinDetailListResponse getAllPinByMe(Users user);

    PinDTO.PinDetailResponse updatePin(Users user, Long pinId, PinDTO.PinUpdateRequest request, List<MultipartFile> pictures) throws ParseException;

    void deletePin(Users user, Long pinId);

    Page<PinDTO.PinDetailResponse> getAllPinsByPocket(Long pocketId, Pageable pageable);

    PinDTO.PinWithDistinctPictureResponse getPictureDetail(Users user, Long pictureId);
}

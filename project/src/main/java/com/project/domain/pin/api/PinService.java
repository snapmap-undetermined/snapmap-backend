package com.project.domain.pin.api;

import com.project.domain.pin.dto.PinDTO;
import com.project.domain.users.entity.Users;
import org.locationtech.jts.io.ParseException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PinService {
    PinDTO.PinDetailResponse createPin(Users user, Long circleId, PinDTO.PinCreateRequest request, List<MultipartFile> pictures);

    PinDTO.PinDetailResponse getPinDetail(Users user, Long feedId);

    List<PinDTO.PinDetailResponse> getAllPinByMe(Users user);

    PinDTO.PinDetailResponse updatePin(Users user, PinDTO.PinUpdateRequest request) throws ParseException;

    void deletePin(Users user, Long feedId);

    List<PinDTO.PinDetailResponse> getAllPinByCircle(Long circleId);
}

package com.project.domain.pin.dto;

import com.project.domain.location.dto.LocationDTO;
import com.project.domain.location.dto.PointDTO;
import com.project.domain.picture.dto.PictureDTO;
import com.project.domain.picture.entity.Picture;
import com.project.domain.pin.entity.Pin;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PinDTO {
    @Data
    @NoArgsConstructor
    public static class PinCreateRequest {
        private String title;
        private String locationName;
        private PointDTO pointDTO;

        public Pin toEntity() throws ParseException {
            return Pin.builder()
                    .title(title)
                    .locationName(locationName)
                    .location(PointDTO.toPoint(pointDTO))
                    .build();
        }
    }

    @Data
    public static class PinUpdateRequest {
        private Long pinId;
        private String title;
        private List<MultipartFile> pictures = new ArrayList<>();
        private String locationName;
        private PointDTO pointDTO;

    }

    @Data
    public static class PinDetailResponse {
        private List<PictureDTO.PictureResponse> pictureList;
        private String locationName;
        private PointDTO location;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public PinDetailResponse(Pin pin, List<Picture> pictureList) {
            this.pictureList = pictureList.stream().map(PictureDTO.PictureResponse::new).collect(Collectors.toList());
            this.locationName = pin.getLocationName();
            this.location = new PointDTO(pin.getLocation().getX(), pin.getLocation().getY());
            this.createdAt = pin.getCreatedAt();
            this.updatedAt = pin.getModifiedAt();
        }
    }

    @Data
    public static class PinDetailListResponse{
        private List<PinDetailResponse> pinDetailResponseList;

        public PinDetailListResponse(List<PinDetailResponse> pinDetailResponseList) {
            this.pinDetailResponseList = pinDetailResponseList;
        }

    }
}


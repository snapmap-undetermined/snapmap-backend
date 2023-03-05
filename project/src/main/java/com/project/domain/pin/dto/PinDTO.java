package com.project.domain.pin.dto;

import com.project.domain.location.dto.LocationDTO;
import com.project.domain.picture.dto.PictureDTO;
import com.project.domain.picture.entity.Picture;
import com.project.domain.pin.entity.Pin;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PinCreateRequest {
        private String title;
        private LocationDTO location;

        public Pin toEntity() throws ParseException {
            return Pin.builder()
                    .title(title)
                    .location(location.toEntity())
                    .build();
        }
    }

    @Data
    @Builder
    public static class PinUpdateRequest {
        private String title;
        private LocationDTO location;
    }

    @Data
    public static class PinDetailResponse {
        private Long id;
        private String title;
        private List<PictureDTO.PictureResponse> pictureList;
        private LocationDTO location;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public PinDetailResponse(Pin pin, List<Picture> pictureList) {
            this.id = pin.getId();
            this.title = pin.getTitle();
            this.pictureList = pictureList.stream().map(PictureDTO.PictureResponse::new).collect(Collectors.toList());
            this.location = new LocationDTO(pin.getLocation());
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


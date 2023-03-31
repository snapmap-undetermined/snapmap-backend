package com.project.domain.pin.dto;

import com.project.domain.location.dto.LocationDTO;
import com.project.domain.picture.dto.PictureDTO;
import com.project.domain.pin.entity.Pin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        private LocationDTO location;

        @Builder.Default
        private List<String> tagNames = new ArrayList<>();

        public Pin toEntity() {
            return Pin.builder()
                    .location(location.toEntity())
                    .activated(true)
                    .build();
        }
    }

    @Data
    @Builder
    public static class PinUpdateRequest {
        private LocationDTO location;
    }

    @Data
    public static class PinDetailResponse {
        private Long id;
        private List<PictureDTO.PictureResponse> pictureList;
        private LocationDTO location;
        private List<String> tags;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public PinDetailResponse(Pin pin) {
            this.id = pin.getId();
            this.pictureList = pin.getPictures().stream().map(PictureDTO.PictureResponse::new).collect(Collectors.toList());
            this.location = new LocationDTO(pin.getLocation());
            this.tags = pin.getPinTags().stream()
                    .map((pinTag -> pinTag.getTag().getName())).toList();
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


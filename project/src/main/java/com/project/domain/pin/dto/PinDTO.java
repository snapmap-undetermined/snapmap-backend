package com.project.domain.pin.dto;

import com.project.common.entity.PagingResponse;
import com.project.domain.comment.dto.PictureCommentDTO;
import com.project.domain.comment.dto.PinCommentDTO;
import com.project.domain.location.dto.LocationDTO;
import com.project.domain.picture.entity.Picture;
import com.project.domain.pin.entity.Pin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

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
                    .build();
        }
    }

    @Data
    @Builder
    public static class PinUpdateRequest {
        @NotNull(message = "위치 정보가 필요합니다.")
        private LocationDTO location;
    }

    @Data
    public static class PinDetailResponse {
        private Long id;
        private List<PinDTO.PinWithDistinctPictureResponse> pictureList;
        private LocationDTO location;
        private List<String> tags;
        private Integer commentCount;
        private PinCommentDTO.PinCommentListResponse commentList;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public PinDetailResponse(Pin pin) {
            this.id = pin.getId();
            this.pictureList = pin.getPictures().stream().map(PinDTO.PinWithDistinctPictureResponse::new).collect(Collectors.toList());
            this.commentList = new PinCommentDTO.PinCommentListResponse(pin.getCommentList().stream().map(PinCommentDTO.PinCommentDetailResponse::new).collect(Collectors.toList()));
            this.location = new LocationDTO(pin.getLocation());
            this.tags = pin.getPinTags().stream().map((pinTag -> pinTag.getTag().getName())).toList();
            this.commentCount = pin.getCommentList().size();
            this.createdAt = pin.getCreatedAt();
            this.updatedAt = pin.getModifiedAt();
        }
    }

    @Data
    public static class PinDetailListResponse {
        private List<PinDetailResponse> pinDetailResponseList;
        private PagingResponse pagingResponse;

        public PinDetailListResponse(List<PinDetailResponse> pinDetailResponseList) {
            this.pinDetailResponseList = pinDetailResponseList;
        }

        public PinDetailListResponse(List<PinDetailResponse> pinDetailListResponse, Page page){
            this.pinDetailResponseList = pinDetailResponseList;
            this.pagingResponse = new PagingResponse(page);
        }
    }

    @Data
    public static class PinWithDistinctPictureResponse {
        private Long id;
        private String uri;
        private Long pinId;
        private String pinName;
        private String originalName;
        private PictureCommentDTO.PictureCommentListResponse pictureCommentList;
        private Integer pictureCommentCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public PinWithDistinctPictureResponse(Picture picture) {
            this.id = picture.getId();
            this.uri = picture.getUrl();
            this.pinId = picture.getPin().getId();
            this.pinName = picture.getPin().getLocation().getName();
            this.originalName = picture.getOriginalName();
            this.pictureCommentList = new PictureCommentDTO.PictureCommentListResponse(picture.getCommentList().stream().map(PictureCommentDTO.PictureCommentDetailResponse::new).collect(Collectors.toList()));
            this.pictureCommentCount = picture.getCommentList().size();
            this.createdAt = picture.getCreatedAt();
            this.updatedAt = picture.getModifiedAt();
        }
    }

}


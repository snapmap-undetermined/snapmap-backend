package com.project.domain.pin.dto;

import com.project.domain.picture.dto.PictureDTO;
import com.project.domain.pin.entity.Pin;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PinDTO {
    @Data
    public static class PinCreateRequest {
        private Double longitude;
        private Double latitude;

        public Pin toEntity() throws ParseException {
            return Pin.builder()
                    .location(toPoint(new PointDTO(longitude, latitude)))
                    .build();
        }
    }

    @Data
    public static class PinUpdateRequest {
        private Long pinId;
        private List<MultipartFile> pictures = new ArrayList<>();
        private PointDTO location;
    }



    @Data
    public static class PinDetailResponse {
        private List<PictureDTO.PictureResponse> pictureList;
        private PointDTO point;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public PinDetailResponse(Pin pin) {
            this.pictureList = pin.getPictureList().stream().map(PictureDTO.PictureResponse::new).collect(Collectors.toList());
            this.point = new PointDTO(pin.getLocation());
            this.createdAt = pin.getCreatedAt();
            this.updatedAt = pin.getModifiedAt();
        }
    }

    @Data
    public static class PinDetailListResponse{
        private List<PinDetailResponse> pinDetailListResponseList;

        public PinDetailListResponse(List<PinDetailResponse> pinDetailListResponseList) {
            this.pinDetailListResponseList = pinDetailListResponseList;
        }
    }




    @Data
    public static class PointDTO {
        private Double longitude; // 경도
        private Double latitude;  // 위도

        public PointDTO(Point point) {
            this.longitude = point.getX();
            this.latitude = point.getY();
        }

        public PointDTO(Double longitude, Double latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }
    }

    public static Point toPoint(PointDTO pointDTO) throws ParseException {
        final String pointWKT = String.format("POINT(%s %s)", pointDTO.getLongitude(), pointDTO.getLongitude());
        return (Point) new WKTReader().read(pointWKT);
    }
}


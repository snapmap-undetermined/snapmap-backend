package com.project.domain.story.dto;

import com.project.domain.picture.dto.PictureDTO;
import com.project.domain.story.entity.Story;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StoryDTO {
    @Data
    public static class StoryCreateRequest {
        private Double longitude;
        private Double latitude;

        public Story toEntity() throws ParseException {
            return Story.builder()
                    .location(toPoint(new PointDTO(longitude, latitude)))
                    .build();
        }
    }

    @Data
    public static class StoryUpdateRequest {
        private List<MultipartFile> pictures = new ArrayList<>();
        private PointDTO location;
    }



    @Data
    public static class StoryDetailResponse {
        private List<PictureDTO.PictureResponse> pictureList;
        private PointDTO point;

        public StoryDetailResponse(Story story) {
            this.pictureList = story.getPictureList().stream().map(PictureDTO.PictureResponse::new).collect(Collectors.toList());
            this.point = new PointDTO(story.getLocation());
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


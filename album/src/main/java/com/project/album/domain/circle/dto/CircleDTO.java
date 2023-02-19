package com.project.album.domain.circle.dto;


import com.project.album.domain.circle.entity.Circle;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CircleDTO {

    @Data
    public static class CircleSimpleInfoResponse {
        private Long circleId;
        private String circleName;

        public CircleSimpleInfoResponse(Circle circle) {
            this.circleId = circle.getId();
            this.circleName = circle.getName();
        }
    }

    @Data
    public static class CreateCircleRequest {
        private String circleName;

        public Circle toEntity() {
            return Circle.builder()
                    .name(circleName)
                    .build();
        }
    }

    @Data
    public static class UpdateCircleRequest {
        private String circleName;
    }
}

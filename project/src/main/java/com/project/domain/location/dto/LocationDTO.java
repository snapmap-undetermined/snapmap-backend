package com.project.domain.location.dto;

import com.project.domain.location.entity.Location;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.io.ParseException;

@Data
@NoArgsConstructor
public class LocationDTO {
    @NotBlank(message = "위치의 이름을 입력해주세요.")
    private String name;
    private PointDTO point;

    public LocationDTO(Location location) {
        this.name = location.getName();
        this.point = new PointDTO(location.getPoint().getX(), location.getPoint().getY());
    }

    public LocationDTO(String name, PointDTO pointDTO) {
        this.name = name;
        this.point = new PointDTO(pointDTO.getLongitude(), pointDTO.getLatitude());
    }

    public Location toEntity() {
        return Location.builder()
                .name(name)
                .point(PointDTO.toPoint(point))
                .build();
    }

}

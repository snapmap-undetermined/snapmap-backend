package com.project.domain.location.dto;

import com.project.common.exception.ErrorCode;
import com.project.common.exception.InvalidValueException;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

@Data
@NoArgsConstructor
public class PointDTO {
    @NotBlank(message = "위도(longitude) 값이 필요합니다.")
    private Double longitude;
    @NotBlank(message = "경도(latitude) 값이 필요합니다.")
    private Double latitude;


    public PointDTO(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public static Point toPoint(PointDTO pointDTO) {
        final String pointWKT = String.format("POINT(%s %s)", pointDTO.getLongitude(), pointDTO.getLatitude());
        try {
            return (Point) new WKTReader().read(pointWKT);
        } catch (ParseException e) {
            throw new InvalidValueException("WKTReader reads point failed.", ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}

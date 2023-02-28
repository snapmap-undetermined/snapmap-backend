package com.project.domain.pin.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.location.dto.PointDTO;
import com.project.domain.location.entity.Location;
import com.project.domain.picture.entity.Picture;
import com.project.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pin")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Pin extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "pin_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user")
    private Users user;

    // 핀 이름
    @Column(name = "title")
    private String title;

    // 하나의 장소에 대응되는 여러 개의 핀이 존재 가능하다.
    @Column(name = "location")
    private Point location;

    @Column(name = "location_name")
    private String locationName;

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateUser(Users user) {
        this.user = user;
    }

    public void updateLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void updateLocation(PointDTO pointDTO) throws ParseException {
        this.location = PointDTO.toPoint(pointDTO);
    }
}

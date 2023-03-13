package com.project.domain.location.entity;

import com.project.common.utils.GeomUtils;
import com.project.domain.friend.entity.Friend;
import com.project.domain.pin.entity.Pin;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "location")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Location {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    // 해당 Point의 별칭
    @Column(name = "name")
    private String name;

    @Column(name = "point")
    private Point point;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Pin> pins = new ArrayList<>();

    public void addPin(Pin pin) {
        if (!this.pins.contains(pin)) {
            this.pins.add(pin);
        }
        pin.setLocation(this);
    }

    public void removePin(Pin pin) {
        this.pins.remove(pin);
        pin.setLocation(null);
    }
}

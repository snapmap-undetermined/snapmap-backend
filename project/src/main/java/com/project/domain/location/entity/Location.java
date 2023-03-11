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
    @Column(name = "location_id")
    private Long id;

    // 해당 Point의 별칭
    @Column(name = "name")
    private String name;

    @Column(name = "point")
    private Point point;

//    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    private List<Pin> pinList = new ArrayList<>();
}

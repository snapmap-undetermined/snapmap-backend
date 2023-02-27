package com.project.domain.circlepin.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.circle.entity.Circle;
import com.project.domain.pin.entity.Pin;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "circle_pin")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class CirclePin extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "circle_pin_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "circle_id")
    private Circle circle;

    @ManyToOne
    @JoinColumn(name = "pin_id")
    private Pin pin;

}

package com.project.domain.picture.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.pin.entity.Pin;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "picture")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Picture extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "picture_id")
    private Long id;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "pin_id")
    private Pin pin;


    public void setPin(Pin pin) {
        this.pin = pin;
        if (!pin.getPictureList().contains(this)) {
            pin.getPictureList().add(this);
        }
    }
}

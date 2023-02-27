package com.project.domain.pinpicture.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.picture.entity.Picture;
import com.project.domain.pin.entity.Pin;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pin_picture")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class PinPicture extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "pin_picture_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pin_id")
    private Pin pin;

    @ManyToOne
    @JoinColumn(name = "picture_id")
    private Picture picture;

}

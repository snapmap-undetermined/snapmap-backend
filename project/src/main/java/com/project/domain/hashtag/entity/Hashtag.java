package com.project.domain.hashtag.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.picture.entity.Picture;
import com.project.domain.pin.entity.Pin;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hashtag")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Hashtag extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "hashtag_id")
    private Long id;

    @Column(name = "name")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

}

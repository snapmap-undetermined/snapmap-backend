package com.project.album.domain.circle.entity;

import com.project.album.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "circle")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Circle extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "circle_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "circle_key")
    private String circleKey;

    public void setCircleKey(String circleKey){
        this.circleKey = circleKey;
    }

    public void setName(String circleName) {
        this.name = circleName;
    }

}

package com.project.domain.picture.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.comment.entity.PictureComment;
import com.project.domain.pin.entity.Pin;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "picture")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Picture extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "url")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pin pin;

    @OneToMany(mappedBy = "picture", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<PictureComment> commentList = new ArrayList<>();

    public void setPin(Pin pin) {
        this.pin = pin;
    }
}

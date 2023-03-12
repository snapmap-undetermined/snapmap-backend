package com.project.domain.pin.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.circle.entity.Circle;
import com.project.domain.location.entity.Location;
import com.project.domain.picture.entity.Picture;
import com.project.domain.pintag.entity.PinTag;
import com.project.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;

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
    private Long id;

    @ManyToOne
    private Users user;

    // 핀 이름
    @Column(name = "title")
    private String title;

    @ManyToOne
    private Circle circle;

    // 하나의 장소에 대응되는 여러 개의 핀이 존재 가능하다.
    @ManyToOne
    private Location location;

    @OneToMany(mappedBy = "pin", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<PinTag> pinTags = new ArrayList<>();

    @OneToMany(mappedBy = "pin", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Picture> pictures = new ArrayList<>();


    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public void addPicture(Picture picture) {
        getPictures().add(picture);
        picture.setPin(this);
    }

    public void removePicture(Picture picture) {
        this.getPictures().remove(picture);
        picture.setPin(null);
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setLocation(Location location) {
        if (this.location != null) {
            this.location.getPins().remove(this);
        }
        this.location = location;
        location.getPins().add(this);
    }
}

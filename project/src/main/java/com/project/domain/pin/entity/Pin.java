package com.project.domain.pin.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.location.entity.Location;
import com.project.domain.pinpicture.entity.PinPicture;
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
    @Column(name = "pin_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user")
    private Users user;

    // 핀 이름
    @Column(name = "title")
    private String title;

    // 하나의 장소에 대응되는 여러 개의 핀이 존재 가능하다.
    @ManyToOne
    @JoinColumn(name = "location")
    private Location location;

    @OneToMany(mappedBy = "pin", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PinTag> pinTags = new ArrayList<>();

    @OneToMany(mappedBy = "pin", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PinPicture> pinPictures = new ArrayList<>();

    public void setPinTags(List<PinTag> pinTags) {
        this.pinTags = pinTags;
    }

    public void setPinPictures(List<PinPicture> pinPictures) {
        this.pinPictures = pinPictures;
    }
    public void updateTitle(String title) {
        this.title = title;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void updateLocation(Location location) {
        this.location = location;

    }
}

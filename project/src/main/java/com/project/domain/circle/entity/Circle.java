package com.project.domain.circle.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.pin.entity.Pin;
import com.project.domain.usercircle.entity.UserCircle;
import com.project.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "circle")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Circle extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @OneToMany(mappedBy = "circle", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Pin> pinList = new ArrayList<>();

    @OneToMany(mappedBy = "circle", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserCircle> userCircleList = new ArrayList<>();

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users master;

    @Column(name = "circlekey")
    private String key;

    @Column(name = "imageUrl")
    private String imageUrl;

    public void addPin(Pin pin) {
        this.getPinList().add(pin);
        pin.setCircle(this);
    }

    public void removePin(Pin pin) {
        this.getPinList().remove(pin);
        pin.setCircle(null);
    }

    public void setName(String circleName) {
        this.name = circleName;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void removeUserCircle(UserCircle userCircle) {
        this.getUserCircleList().remove(userCircle);
        userCircle.setCircle(null);
    }

    public void setMaster(Users user) {
        this.master = user;
    }

    public void setImageUrl(String url) {
        this.imageUrl = url;
    }

}

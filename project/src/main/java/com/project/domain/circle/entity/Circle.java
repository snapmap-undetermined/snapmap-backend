package com.project.domain.circle.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.pin.entity.Pin;
import com.project.domain.usercircle.entity.UserCircle;
import com.project.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    @OneToOne(fetch = FetchType.LAZY)
    private Users master;

    private String description;

    @Column(name = "circle_key")
    private String circleKey;

    @Column(name = "image_url")
    private String imageUrl;

    public void addPin(Pin pin) {
        if (!getPinList().contains(pin)) {
            getPinList().add(pin);
        }
        pin.setCircle(this);
    }

    public void removePin(Pin pin) {
        this.getPinList().remove(pin);
        pin.setCircle(null);
    }

    public void setName(String circleName) {
        this.name = circleName;
    }

    public void setMaster(Users user) {
        this.master = user;
    }

    public void setImageUrl(String url) {
        this.imageUrl = url;
    }

    public void setCircleKey(String key) {
        this.circleKey = key;
    }
    public String generateCircleKey() {
        Random random = new Random();
        return random.ints(48, 122 + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}

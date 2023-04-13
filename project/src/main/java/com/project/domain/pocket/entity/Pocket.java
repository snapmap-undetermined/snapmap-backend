package com.project.domain.pocket.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.pin.entity.Pin;
import com.project.domain.userpocket.entity.UserPocket;
import com.project.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "pocket")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Pocket extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @OneToMany(mappedBy = "pocket", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Pin> pinList = new ArrayList<>();

    @OneToMany(mappedBy = "pocket", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserPocket> userPocketList = new ArrayList<>();

    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    private Users master;

    private String description;

    @Column(name = "pocket_key")
    private String pocketKey;

    @Column(name = "image_url")
    private String imageUrl;

    public void addPin(Pin pin) {
        if (!getPinList().contains(pin)) {
            getPinList().add(pin);
        }
        pin.setPocket(this);
    }

    public void removePin(Pin pin) {
        this.getPinList().remove(pin);
        pin.setPocket(null);
    }

    public void setName(String pocketName) {
        this.name = pocketName;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setMaster(Users user) {
        this.master = user;
    }

    public void setImageUrl(String url) {
        this.imageUrl = url;
    }

    public void setPocketKey(String key) {
        this.pocketKey = key;
    }

    public String generatePocketKey() {
        Random random = new Random();
        return random.ints(48, 122 + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}

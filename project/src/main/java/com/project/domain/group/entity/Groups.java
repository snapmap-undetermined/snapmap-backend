package com.project.domain.group.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.pin.entity.Pin;
import com.project.domain.usergroup.entity.UserGroup;
import com.project.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "groups")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Groups extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Pin> pinList = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserGroup> userGroupList = new ArrayList<>();

    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    private Users master;

    private String description;

    @Column(name = "group_key")
    private String groupKey;

    @Column(name = "image_url")
    private String imageUrl;

    public void addPin(Pin pin) {
        if (!getPinList().contains(pin)) {
            getPinList().add(pin);
        }
        pin.setGroup(this);
    }

    public void removePin(Pin pin) {
        this.getPinList().remove(pin);
        pin.setGroup(null);
    }

    public void setName(String groupName) {
        this.name = groupName;
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

    public void setGroupKey(String key) {
        this.groupKey = key;
    }

    public String generateGroupKey() {
        Random random = new Random();
        return random.ints(48, 122 + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}

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

    @OneToMany(mappedBy = "circle", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Pin> pinList = new ArrayList<>();

    @OneToMany(mappedBy = "circle", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserCircle> userList = new ArrayList<>();

    @Column(name = "name")
    private String name;

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

    public void addUser(UserCircle userCircle) {
        this.getUserList().add(userCircle);
        userCircle.setCircle(this);
    }

    public void removeUser(UserCircle userCircle) {
        this.getUserList().remove(userCircle);
        userCircle.setCircle(null);
    }

}

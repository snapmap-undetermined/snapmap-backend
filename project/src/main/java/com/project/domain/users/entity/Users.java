package com.project.domain.users.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.common.entity.Role;
import com.project.domain.friend.entity.Friend;
import com.project.domain.pin.entity.Pin;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Users extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Pin> pinList = new ArrayList<>();

    @OneToMany(mappedBy = "me", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Friend> friendList = new ArrayList<>();

    @OneToOne
    private RefreshToken refreshToken;

    public void addPin(Pin pin) {
        if (!this.pinList.contains(pin)) {
            this.pinList.add(pin);
        }
        pin.setUser(this);
    }

    public void removePin(Pin pin) {
        this.pinList.remove(pin);
        pin.setUser(null);
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}

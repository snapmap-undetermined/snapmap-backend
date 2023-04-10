package com.project.domain.users.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.common.entity.Role;
import com.project.domain.friend.entity.Friend;
import com.project.domain.pin.entity.Pin;
import com.project.domain.usergroup.entity.UserGroup;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Where(clause = "activated = 1")
@SQLDelete(sql = "UPDATE user SET activated = 0 where id = ?")
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

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<UserGroup> userGroupList = new ArrayList<>();

    @OneToOne
    private RefreshToken refreshToken;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean activated;

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

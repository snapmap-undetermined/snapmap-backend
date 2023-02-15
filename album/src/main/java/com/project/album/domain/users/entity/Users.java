package com.project.album.domain.users.entity;

import com.project.album.common.entity.BaseTimeEntity;
import com.project.album.common.entity.Role;
import com.project.album.domain.friend.entity.Friend;
import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "user_id")
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

//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Group> groupList;
//
//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Story> storyList;

    @OneToMany(mappedBy = "me", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> friendList;

    @OneToOne
    @JoinColumn(name = "refresh_token_id")
    private RefreshToken refreshToken;
}

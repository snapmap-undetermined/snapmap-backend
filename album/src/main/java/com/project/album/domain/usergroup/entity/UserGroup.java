package com.project.album.domain.usergroup.entity;

import com.project.album.common.entity.BaseTimeEntity;
import com.project.album.domain.circle.entity.Circle;
import com.project.album.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_group")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class UserGroup extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_group_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Circle group;
}

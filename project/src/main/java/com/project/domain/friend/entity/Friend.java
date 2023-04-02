package com.project.domain.friend.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "friend")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Friend extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users me;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users mate;

    @Column(name = "friend_name")
    private String friendName;

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public void setMate(Users mate) {
        this.mate = mate;
    }

    public void setMe(Users me) {
        this.me = me;
    }
}

package com.project.domain.friend.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "friend")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Friend extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "friend_id")
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "user_id")
    private Users me;

    @ManyToOne
    @JoinColumn(referencedColumnName = "user_id")
    private Users mate;

    @Column(name = "friend_name")
    private String friendName;

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public void setFriendUser(Users mate) {
        this.mate = mate;
    }

    public void setMeUser(Users me) {
        this.me = me;
    }
}

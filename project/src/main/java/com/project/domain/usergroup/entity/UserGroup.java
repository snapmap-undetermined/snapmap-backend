package com.project.domain.usergroup.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.group.entity.GroupData;
import com.project.domain.users.entity.Users;
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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    private GroupData group;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean activated;

    public void addUserGroupToUserAndGroup(Users user, GroupData group) {
        addUserGroupToUser(user);
        addUserGroupToGroup(group);
        setUser(user);
        setGroup(group);
    }

    public void removeUserGroupFromUserAndGroup(Users user, GroupData group) {
        removeUserGroupFromGroup(group);
        removeUserGroupFromUser(user);
        setUser(null);
        setGroup(null);
    }


    private void addUserGroupToUser(Users user) {
        if(!user.getUserGroupList().contains(this)){
            user.getUserGroupList().add(this);
        }
    }

    private void removeUserGroupFromUser(Users user) {
        user.getUserGroupList().remove(this);

    }

    private void addUserGroupToGroup(GroupData group) {
        if(!group.getUserGroupList().contains(this)){
            group.getUserGroupList().add(this);
        }
    }

    private void removeUserGroupFromGroup(GroupData group) {
        group.getUserGroupList().remove(this);
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setGroup(GroupData group) {
        this.group = group;
    }

    public void setActivated(Boolean status) {
        status = true;
        this.activated = status;
    }
}

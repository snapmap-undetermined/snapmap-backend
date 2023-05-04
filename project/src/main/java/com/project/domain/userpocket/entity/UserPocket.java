package com.project.domain.userpocket.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.pocket.entity.Pocket;
import com.project.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_pocket")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class UserPocket extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pocket pocket;

    private Boolean activated;

    public void addUserPocketToUserAndPocket(Users user, Pocket pocket) {
        addUserPocketToUser(user);
        addUserPocketToPocket(pocket);
        setUser(user);
        setPocket(pocket);
    }

    public void removeUserPocketFromUserAndPocket(Users user, Pocket pocket) {
        removeUserPocketFromPocket(pocket);
        removeUserPocketFromUser(user);
        setUser(null);
        setPocket(null);
    }


    private void addUserPocketToUser(Users user) {
        if(!user.getUserPocketList().contains(this)){
            user.getUserPocketList().add(this);
        }
    }

    private void removeUserPocketFromUser(Users user) {
        user.getUserPocketList().remove(this);

    }

    private void addUserPocketToPocket(Pocket pocket) {
        if(!pocket.getUserPocketList().contains(this)){
            pocket.getUserPocketList().add(this);
        }
    }

    private void removeUserPocketFromPocket(Pocket pocket) {
        pocket.getUserPocketList().remove(this);
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setPocket(Pocket pocket) {
        this.pocket = pocket;
    }

    public void setActivated(Boolean status) {
        status = true;
        this.activated = status;
    }
}

package com.project.domain.usercircle.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.circle.entity.Circle;
import com.project.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "user_circle")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE user_circle SET activated = 0 where id = ?")
public class UserCircle extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Circle circle;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean activated;

    public void addUserCircleToUserAndCircle(Users user, Circle circle) {
        addUserCircleToUser(user);
        addUserCircleToCircle(circle);
        setUser(user);
        setCircle(circle);
    }

    public void removeUserCircleFromUserAndCircle(Users user, Circle circle) {
        removeUserCircleFromCircle(circle);
        removeUserCircleFromUser(user);
        setUser(null);
        setCircle(null);
    }


    private void addUserCircleToUser(Users user) {
        if(!user.getUserCircleList().contains(this)){
            user.getUserCircleList().add(this);
        }
    }

    private void removeUserCircleFromUser(Users user) {
        user.getUserCircleList().remove(this);

    }

    private void addUserCircleToCircle(Circle circle) {
        if(!circle.getUserCircleList().contains(this)){
            circle.getUserCircleList().add(this);
        }
    }

    private void removeUserCircleFromCircle(Circle circle) {
        circle.getUserCircleList().remove(this);
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public void setActivated(Boolean status) {
        status = true;
        this.activated = status;
    }
}

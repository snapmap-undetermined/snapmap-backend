package com.project.domain.usercircle.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.circle.entity.Circle;
import com.project.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "user_circle")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Where(clause = "isActive = 1")
public class UserCircle extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Circle circle;

    private int isActive;

    public void setUserAndCircle(Users user, Circle circle) {
        addUserCircleForUser(user);
        addUserCircleForCircle(circle);
        setUser(user);
        setCircle(circle);
    }

    private void addUserCircleForUser(Users user) {
        if(!user.getUserCircleList().contains(this)){
            user.getUserCircleList().add(this);
        }
    }

    private void addUserCircleForCircle(Circle circle) {
        if(!circle.getUserCircleList().contains(this)){
            circle.getUserCircleList().add(this);
        }
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public void setIsActive() {
        this.isActive = 1;
    }

}

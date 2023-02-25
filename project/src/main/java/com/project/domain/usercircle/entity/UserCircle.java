package com.project.domain.usercircle.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.circle.entity.Circle;
import com.project.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_circle")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class UserCircle extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_circle_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "circle_id")
    private Circle circle;

    public void setUser(Users user) {
        this.user = user;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }
}

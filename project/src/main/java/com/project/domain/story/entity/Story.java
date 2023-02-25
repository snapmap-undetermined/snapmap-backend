package com.project.domain.story.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.picture.entity.Picture;
import com.project.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "story")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Story extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "story_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user")
    private Users user;

    @OneToMany(mappedBy = "story", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<Picture> pictureList = new ArrayList<>();

    @Column(name = "location")
    private Point location;

    public void addPicture(Picture picture){
        this.pictureList.add(picture);
        if(picture.getStory() != this) picture.setStory(this);
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setLocation(Point point) {
        this.location = point;
    }
}

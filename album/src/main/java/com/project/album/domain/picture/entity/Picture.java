package com.project.album.domain.picture.entity;

import com.project.album.common.entity.BaseTimeEntity;
import com.project.album.domain.story.entity.Story;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "picture")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Picture extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "picture_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "story_id")
    private Story story;

    @Column(name="uri")
    private String uri;

    public void setStory(Story story) {
        this.story = story;
        if (!story.getPictureList().contains(this)) {
            story.getPictureList().add(this);
        }
    }
}

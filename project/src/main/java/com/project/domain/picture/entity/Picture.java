package com.project.domain.picture.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.story.entity.Story;
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

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "story_id")
    private Story story;


    public void setStory(Story story) {
        this.story = story;
        if (!story.getPictureList().contains(this)) {
            story.getPictureList().add(this);
        }
    }
}

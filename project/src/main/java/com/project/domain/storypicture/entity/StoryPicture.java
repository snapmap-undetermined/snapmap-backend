package com.project.domain.storypicture.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.picture.entity.Picture;
import com.project.domain.story.entity.Story;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "story_picture")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class StoryPicture extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "story_picture_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "story_id")
    private Story story;

    @ManyToOne
    @JoinColumn(name = "picture_id")
    private Picture picture;

}

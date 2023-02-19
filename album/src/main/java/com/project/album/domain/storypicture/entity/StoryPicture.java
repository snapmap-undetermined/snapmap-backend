package com.project.album.domain.storypicture.entity;

import com.project.album.common.entity.BaseTimeEntity;
import com.project.album.domain.picture.entity.Picture;
import com.project.album.domain.story.entity.Story;
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

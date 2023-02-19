package com.project.album.domain.circlestory.entity;

import com.project.album.common.entity.BaseTimeEntity;
import com.project.album.domain.circle.entity.Circle;
import com.project.album.domain.story.entity.Story;
import com.project.album.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "circle_story")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class CircleStory extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "circle_story_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "circle_id")
    private Circle circle;

    @ManyToOne
    @JoinColumn(name = "story_id")
    private Story story;

}

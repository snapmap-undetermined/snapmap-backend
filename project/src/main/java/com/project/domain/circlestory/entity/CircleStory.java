package com.project.domain.circlestory.entity;

import com.project.common.entity.BaseTimeEntity;
import com.project.domain.circle.entity.Circle;
import com.project.domain.story.entity.Story;
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

package com.project.album.domain.userstory.entity;

import com.project.album.common.entity.BaseTimeEntity;
import com.project.album.domain.story.entity.Story;
import com.project.album.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_story")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class UserStory extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_story_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "story_id")
    private Story story;

}

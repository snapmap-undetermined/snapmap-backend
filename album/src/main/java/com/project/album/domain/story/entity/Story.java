package com.project.album.domain.story.entity;

import com.project.album.common.entity.BaseTimeEntity;
import com.project.album.domain.picture.entity.Picture;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(mappedBy = "story", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Picture> pictureList;

}

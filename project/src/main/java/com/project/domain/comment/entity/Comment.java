package com.project.domain.comment.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comment")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Comment {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "comment_id")
    private Long id;


}

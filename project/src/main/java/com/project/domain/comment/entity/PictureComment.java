package com.project.domain.comment.entity;


import com.project.common.entity.BaseTimeEntity;
import com.project.domain.picture.entity.Picture;
import com.project.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "picture_comment")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class PictureComment extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "picture_comment_id")
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "picture_id")
    private Picture picture;

    @Column
    private Long parentCommentNum; // 자신이 속한 원댓글의 번호

    @Column
    private Long commentOrder; // 자신의 댓글 번호

    @Column
    private Long childCommentCount; // 자식 댓글의 개수

    @Column(nullable = false)
    private Boolean isDeleted; // 댓글 삭제 여부

    @Column
    private Integer likeCount; // 댓글 좋아요

    @Column
    private Integer hateCount; // 댓글 싫어요




}

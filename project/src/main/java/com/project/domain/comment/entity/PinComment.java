package com.project.domain.comment.entity;


import com.project.common.entity.BaseTimeEntity;
import com.project.domain.pin.entity.Pin;
import com.project.domain.users.entity.Users;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "pin_comment")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class PinComment extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "pin_comment_id")
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pin_id")
    private Pin pin;

    @Column
    private Long parentCommentId; // 원댓글의 번호

    @Column
    private Long commentOrder; // 순서

    @Column
    private Long childCommentCount; // 자식 댓글의 개수

    @Column(nullable = false)
    private Boolean isDeleted; // 댓글 삭제 여부

    public void setDeleted() {
        this.isDeleted = true;
    }

    public void plusChildCommentCount() {
        this.childCommentCount++;
    }

    public void minusChildCommentCount() {
        this.childCommentCount--;
    }

    public void setCommentOrder(Long order) {
        this.commentOrder = order;
    }

    public void setParentCommentId(Long pinCommentId) {
        this.commentOrder = pinCommentId;
    }

    public void setText(String text) {
        this.text = text;

    }


}

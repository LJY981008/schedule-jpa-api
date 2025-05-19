package com.example.schedulejpaapi.entity;

import com.example.schedulejpaapi.dto.comment.CommentCreateRequestDto;
import jakarta.persistence.*;
import lombok.Getter;

/**
 * 댓글 정보 관리 Entity 클래스
 */
@Entity
@Getter
@Table(name = "comment")
public class Comment extends TimeStampEntity {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contents", nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Comment() {
    }

    public Comment(CommentCreateRequestDto requestDto, Post post, Member member) {
        this.contents = requestDto.getContents();
        this.post = post;
        this.member = member;
    }

    public void updateContents(String value) {
        this.contents = value;
    }
}

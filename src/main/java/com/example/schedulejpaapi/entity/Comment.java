package com.example.schedulejpaapi.entity;

import com.example.schedulejpaapi.dto.comment.CommentCreateRequestDto;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "comment")
public class Comment extends TimeStampEntity{
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contents", nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Comment() {
    }

    public Comment(CommentCreateRequestDto requestDto, Post post, Member member) {
        this.contents = requestDto.getContents();
        this.post = post;
        this.member = member;

        post.getComments().add(this);
        member.getComments().add(this);
    }
}

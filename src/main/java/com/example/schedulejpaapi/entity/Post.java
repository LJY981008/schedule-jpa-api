package com.example.schedulejpaapi.entity;


import com.example.schedulejpaapi.dto.post.PostCreateRequestDto;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 스케줄 정보 관리 Entity 클래스
 */
@Entity
@Getter
@Table(name = "post")
public class Post extends TimeStampEntity {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "contents", nullable = false, columnDefinition = "TEXT")
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    public Post() {
    }

    public Post(PostCreateRequestDto requestDto, Member member) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.member = member;
        this.comments = new ArrayList<>();
    }

    public void updateTitle(String value) {
        this.title = value;
    }

    public void updateContents(String value) {
        this.contents = value;
    }
}

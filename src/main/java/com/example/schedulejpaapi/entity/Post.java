package com.example.schedulejpaapi.entity;


import com.example.schedulejpaapi.dto.post.PostCreateRequestDto;
import com.example.schedulejpaapi.dto.post.PostUpdateRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Entity
@Getter
@Table(name = "post")
public class Post extends TimeStampEntity {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    @Size(min = 1, max = 10)
    @NotBlank(message = "title is empty")
    private String title;

    @Column(name = "contents", nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "contents is empty")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Post() {
    }

    public Post(PostCreateRequestDto requestDto, Member member) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.member = member;
        member.getPost().add(this);
    }

    public void updateTitle(String value) {
        this.title = value;
    }

    public void updateContents(String value) {
        this.contents = value;
    }
}

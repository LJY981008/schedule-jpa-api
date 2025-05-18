package com.example.schedulejpaapi.entity;


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

    public Post(String title, String contents, Member member) {
        this.title = title;
        this.contents = contents;
        this.member = member;
    }
}

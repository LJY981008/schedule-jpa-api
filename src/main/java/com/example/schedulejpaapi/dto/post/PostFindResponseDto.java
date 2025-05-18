package com.example.schedulejpaapi.dto.post;

import com.example.schedulejpaapi.entity.Post;
import lombok.Getter;

// 스케줄 탐색 응답
@Getter
public class PostFindResponseDto {
    private Long postId;
    private String title;
    private String contents;
    private String createdAt;
    private String modifiedAt;
    private String memberName;

    public PostFindResponseDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.createdAt = post.getCreatedAt().toString();
        this.modifiedAt = post.getModifiedAt().toString();
        this.memberName = post.getMember().getName();
    }
}

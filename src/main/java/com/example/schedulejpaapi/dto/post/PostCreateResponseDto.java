package com.example.schedulejpaapi.dto.post;

import com.example.schedulejpaapi.entity.Post;
import lombok.Getter;

/**
 * 스케줄 생성 응답 DTO
 */
@Getter
public class PostCreateResponseDto {

    private Long postId;
    private String title;
    private String contents;
    private String createdAt;
    private String modifiedAt;
    private Long memberId;
    private String memberName;

    public PostCreateResponseDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.createdAt = post.getCreatedAt().toString();
        this.modifiedAt = post.getModifiedAt().toString();
        this.memberId = post.getMember().getId();
        this.memberName = post.getMember().getName();
    }
}

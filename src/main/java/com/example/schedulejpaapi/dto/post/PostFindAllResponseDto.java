package com.example.schedulejpaapi.dto.post;

import com.example.schedulejpaapi.entity.Post;
import lombok.Getter;

/**
 * 스케줄 목록 조회 응답 DTO
 */
@Getter
public class PostFindAllResponseDto {

    private String title;
    private String contents;
    private String commentCount;
    private String createdAt;
    private String modifiedAt;
    private String memberName;

    public PostFindAllResponseDto(Post post) {
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.commentCount = String.valueOf(post.getComments().size());
        this.createdAt = post.getCreatedAt().toString();
        this.modifiedAt = post.getModifiedAt().toString();
        this.memberName = post.getMember().getName();
    }
}

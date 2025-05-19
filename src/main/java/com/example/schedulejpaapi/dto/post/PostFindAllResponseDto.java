package com.example.schedulejpaapi.dto.post;

import com.example.schedulejpaapi.entity.Post;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PostFindAllResponseDto {
    private Long postId;
    private String title;

    public PostFindAllResponseDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
    }
}

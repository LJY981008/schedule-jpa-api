package com.example.schedulejpaapi.dto.post;

import com.example.schedulejpaapi.entity.Post;
import lombok.Getter;

@Getter
public class PostRemoveResponseDto {
    private Long postId;

    public PostRemoveResponseDto(Post post) {
        this.postId = post.getId();
    }
}

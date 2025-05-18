package com.example.schedulejpaapi.dto.post;

import com.example.schedulejpaapi.entity.Post;
import lombok.Getter;

// 스케줄 삭제 응답
@Getter
public class PostRemoveResponseDto {
    private Long postId;

    public PostRemoveResponseDto(Post post) {
        this.postId = post.getId();
    }
}

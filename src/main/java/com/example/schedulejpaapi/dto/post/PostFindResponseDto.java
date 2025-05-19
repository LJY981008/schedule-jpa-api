package com.example.schedulejpaapi.dto.post;

import com.example.schedulejpaapi.entity.Comment;
import com.example.schedulejpaapi.entity.Post;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 스케줄 탐색 응답
@Getter
public class PostFindResponseDto {
    private Long postId;
    private String title;
    private String contents;
    private String createdAt;
    private String modifiedAt;
    private String memberName;
    private Map<Long, String> commentMap;

    public PostFindResponseDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.createdAt = post.getCreatedAt().toString();
        this.modifiedAt = post.getModifiedAt().toString();
        this.memberName = post.getMember().getName();

        this.commentMap = new HashMap<>();
        for (Comment comment : post.getComments()) {
            this.commentMap.put(comment.getId(), comment.getContents());
        }
    }
}

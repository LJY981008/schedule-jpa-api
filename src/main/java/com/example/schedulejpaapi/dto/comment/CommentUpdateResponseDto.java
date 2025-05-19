package com.example.schedulejpaapi.dto.comment;

import com.example.schedulejpaapi.entity.Comment;
import lombok.Getter;

@Getter
public class CommentUpdateResponseDto {
    private Long commentId;
    private String contents;
    private String createdAt;
    private String modifiedAt;
    private Long postId;
    private String memberName;

    public CommentUpdateResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt().toString();
        this.modifiedAt = comment.getModifiedAt().toString();
        this.postId = comment.getPost().getId();
        this.memberName = comment.getMember().getName();
    }
}

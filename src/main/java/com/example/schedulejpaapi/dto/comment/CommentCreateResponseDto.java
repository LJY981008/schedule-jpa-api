package com.example.schedulejpaapi.dto.comment;

import com.example.schedulejpaapi.entity.Comment;
import lombok.Getter;

/**
 * 댓글 생성 응답 DTO
 */
@Getter
public class CommentCreateResponseDto {

    private Long commentId;
    private String contents;
    private String createdAt;
    private String modifiedAt;
    private Long postId;
    private String memberName;

    public CommentCreateResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.contents = comment.getContents();
        this.createdAt = comment.getCreatedAt().toString();
        this.modifiedAt = comment.getModifiedAt().toString();
        this.postId = comment.getPost().getId();
        this.memberName = comment.getMember().getName();
    }
}

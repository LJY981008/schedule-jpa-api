package com.example.schedulejpaapi.dto.comment;

import com.example.schedulejpaapi.entity.Comment;
import lombok.Getter;

/**
 * 댓글 삭제 응답 DTO
 */
@Getter
public class CommentRemoveResponseDto {

    private Long commentId;
    private Long postId;
    private String memberName;

    public CommentRemoveResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.postId = comment.getPost().getId();
        this.memberName = comment.getMember().getName();
    }
}

package com.example.schedulejpaapi.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * 댓글 생성 요청 DTO
 */
@Getter
public class CommentCreateRequestDto {

    @NotBlank(message = "comment is empty")
    private String contents;
}

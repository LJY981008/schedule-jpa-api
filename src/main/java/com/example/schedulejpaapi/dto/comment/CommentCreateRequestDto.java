package com.example.schedulejpaapi.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentCreateRequestDto {

    @NotBlank(message = "contents is empty")
    private String contents;
}

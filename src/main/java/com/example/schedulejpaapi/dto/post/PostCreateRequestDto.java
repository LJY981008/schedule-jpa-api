package com.example.schedulejpaapi.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class PostCreateRequestDto {

    @NotBlank
    @Size(min = 1, max = 10)
    private String title;

    @NotBlank
    private String contents;
}

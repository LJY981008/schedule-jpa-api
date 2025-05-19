package com.example.schedulejpaapi.dto.comment;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.Map;

@Getter
public class CommentUpdateRequestDto {
    private Map<String, String> updateMap;

    @AssertTrue(message = "nothing to change")
    public boolean isNothingToUpdate() {
        return updateMap != null && !updateMap.isEmpty();
    }

    public CommentUpdateRequestDto() {
    }
}

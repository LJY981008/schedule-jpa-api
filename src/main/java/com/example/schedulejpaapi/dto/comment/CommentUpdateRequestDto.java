package com.example.schedulejpaapi.dto.comment;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;

import java.util.Map;

/**
 * 댓글 수정 요청 DTO
 */
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

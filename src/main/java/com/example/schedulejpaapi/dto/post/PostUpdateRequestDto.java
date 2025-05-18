package com.example.schedulejpaapi.dto.post;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;

import java.util.Map;

// 스케줄 수정 요청
@Getter
public class PostUpdateRequestDto {

    private Map<String, String> updateMap;

    @AssertTrue(message = "nothing to change")
    public boolean isNothingToUpdate() {
        return updateMap != null && !updateMap.isEmpty();
    }
}

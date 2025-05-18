package com.example.schedulejpaapi.dto.member;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;

import java.util.Map;

@Getter
public class MemberUpdateRequestDto {

    private Map<String, String> updateMap;

    @AssertTrue(message = "nothing to change")
    public boolean isNothingToUpdate() {
        return updateMap != null && !updateMap.isEmpty();
    }
}

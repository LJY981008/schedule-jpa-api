package com.example.schedulejpaapi.dto;

import com.example.schedulejpaapi.entity.Member;
import lombok.Getter;

@Getter
public class MemberSignupResponseDto {
    private Long memberId;
    private String account;
    private String name;
    private String email;
    private String createdAt;
    private String modifiedAt;

    public MemberSignupResponseDto(Member member) {
        this.memberId = member.getId();
        this.account = member.getAccount();
        this.name = member.getName();
        this.email = member.getEmail();
        this.createdAt = member.getCreatedAt().toString();
        this.modifiedAt = member.getModifiedAt().toString();
    }
}

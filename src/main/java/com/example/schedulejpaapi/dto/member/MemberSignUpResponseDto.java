package com.example.schedulejpaapi.dto.member;

import com.example.schedulejpaapi.entity.Member;
import lombok.Getter;

@Getter
public class MemberSignUpResponseDto {
    private Long memberId;
    private String account;
    private String name;
    private String email;
    private String createdAt;
    private String modifiedAt;

    public MemberSignUpResponseDto(Member member) {
        this.memberId = member.getId();
        this.account = member.getAccount();
        this.name = member.getName();
        this.email = member.getEmail();
        this.createdAt = member.getCreatedAt().toString();
        this.modifiedAt = member.getModifiedAt().toString();
    }
}

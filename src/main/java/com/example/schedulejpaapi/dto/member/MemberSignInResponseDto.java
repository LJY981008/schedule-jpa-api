package com.example.schedulejpaapi.dto.member;

import com.example.schedulejpaapi.entity.Member;
import lombok.Getter;

@Getter
public class MemberSignInResponseDto {
    private Long memberId;
    private String account;
    private String name;

    public MemberSignInResponseDto(Member member) {
        this.memberId = member.getId();
        this.account = member.getAccount();
        this.name = member.getName();
    }
}

package com.example.schedulejpaapi.dto.member;

import com.example.schedulejpaapi.entity.Member;
import lombok.Getter;

// 로그인 응답
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

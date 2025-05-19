package com.example.schedulejpaapi.dto.member;

import com.example.schedulejpaapi.entity.Member;
import lombok.Getter;

// 로그아웃 응답
@Getter
public class MemberLogoutResponseDto {
    private String account;
    private String name;

    public MemberLogoutResponseDto(Member member) {
        this.account = member.getAccount();
        this.name = member.getName();
    }
}

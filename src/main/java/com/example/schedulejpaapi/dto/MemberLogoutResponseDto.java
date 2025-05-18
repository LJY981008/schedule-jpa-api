package com.example.schedulejpaapi.dto;

import com.example.schedulejpaapi.entity.Member;
import lombok.Getter;

@Getter
public class MemberLogoutResponseDto {
    private String account;
    private String name;

    public MemberLogoutResponseDto(Member member) {
        this.account = member.getAccount();
        this.name = member.getName();
    }
}

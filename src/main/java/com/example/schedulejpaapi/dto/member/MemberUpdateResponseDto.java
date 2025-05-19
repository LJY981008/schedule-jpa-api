package com.example.schedulejpaapi.dto.member;

import com.example.schedulejpaapi.entity.Member;
import lombok.Getter;

/**
 * 회원 수정 응답 DTO
 */
@Getter
public class MemberUpdateResponseDto {

    private String account;
    private String name;
    private String createdAt;
    private String modifiedAt;

    public MemberUpdateResponseDto(Member member) {
        this.account = member.getAccount();
        this.name = member.getName();
        this.createdAt = member.getCreatedAt().toString();
        this.modifiedAt = member.getModifiedAt().toString();
    }
}

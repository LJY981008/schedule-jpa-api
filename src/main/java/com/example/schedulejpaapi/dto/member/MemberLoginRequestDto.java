package com.example.schedulejpaapi.dto.member;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

// 로그인 요청
@Getter
public class MemberLoginRequestDto {
    private String account;
    private String email;
    @NotBlank(message = "password is empty")
    private String password;

    @AssertTrue(message = "account and email is empty")
    public boolean isAccountOrEmailNotEmpty() {
        return account != null || email != null;
    }

}

package com.example.schedulejpaapi.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberSignupRequestDto {
    @NotBlank(message = "account is empty")
    private String account;

    @NotBlank(message = "password is empty")
    private String password;

    @NotBlank(message = "userName is empty")
    private String name;

    @Email(message = "incorrect format of email")
    private String email;
}

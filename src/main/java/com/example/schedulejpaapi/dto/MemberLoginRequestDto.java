package com.example.schedulejpaapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberLoginRequestDto {
    @NotBlank(message = "account is empty")
    private String account;
    @NotBlank(message = "password is empty")
    private String password;
}

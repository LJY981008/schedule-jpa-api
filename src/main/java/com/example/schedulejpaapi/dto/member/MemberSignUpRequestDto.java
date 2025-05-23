package com.example.schedulejpaapi.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/**
 * 회원 생성 요청 DTO
 */
@Getter
public class MemberSignUpRequestDto {

    @NotBlank(message = "account is empty")
    private String account;

    @NotBlank(message = "password is empty")
    private String password;

    @NotBlank(message = "userName is empty")
    @Size(min = 1, max = 4)
    private String name;

    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$",
            message = "incorrect format of email")
    private String email;

    public MemberSignUpRequestDto(String account, String password, String name, String email) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.email = email;
    }
}

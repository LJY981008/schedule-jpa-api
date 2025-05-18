package com.example.schedulejpaapi.entity;

import com.example.schedulejpaapi.dto.member.MemberSignupRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
@Table(name = "member")
public class Member extends TimeStampEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account", nullable = false, unique = true)
    @NotBlank(message = "account is empty")
    private String account;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "password is empty")
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    @NotBlank(message = "email is empty")
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$",
            message = "incorrect format of email")
    private String email;

    @Column(name = "name", nullable = false)
    @Size(min = 1, max = 4)
    @NotBlank(message = "name is empty")
    private String name;

    @OneToMany(mappedBy = "member")
    private List<Post> post;


    public Member() {
    }

    public Member(MemberSignupRequestDto requestDto) {
        this.account = requestDto.getAccount();
        this.password = requestDto.getPassword();
        this.email = requestDto.getEmail();
        this.name = requestDto.getName();
    }

    public void updateName(String value){
        this.name = value;
    }
    public void updatePassword(String value){
        this.password = value;
    }
}

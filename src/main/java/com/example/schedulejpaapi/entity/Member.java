package com.example.schedulejpaapi.entity;

import com.example.schedulejpaapi.dto.member.MemberSignUpRequestDto;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

// 회원정보 엔티티
@Entity
@Getter
@Table(name = "member")
public class Member extends TimeStampEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account", nullable = false, unique = true)
    private String account;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "member")
    private List<Post> posts;


    public Member() {
    }

    public Member(MemberSignUpRequestDto requestDto, String password) {
        this.account = requestDto.getAccount();
        this.email = requestDto.getEmail();
        this.name = requestDto.getName();
        this.password = password;
        this.posts = new ArrayList<>();
    }

    public void updateName(String value) {
        this.name = value;
    }

    public void updatePassword(String value) {
        this.password = value;
    }
}

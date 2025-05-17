package com.example.schedulejpaapi.controller;

import com.example.schedulejpaapi.dto.MemberSignupRequestDto;
import com.example.schedulejpaapi.dto.MemberSignupResponseDto;
import com.example.schedulejpaapi.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원 가입
    //TODO 쿠키세션 또는 토큰 이벤트 구현 필요
    @PostMapping("/signup")
    public ResponseEntity<MemberSignupResponseDto> signup(
            @Valid @RequestBody MemberSignupRequestDto requestDto
    ) {
        MemberSignupResponseDto result = memberService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

}

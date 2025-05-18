package com.example.schedulejpaapi.controller;

import com.example.schedulejpaapi.constant.Const;
import com.example.schedulejpaapi.dto.member.*;
import com.example.schedulejpaapi.entity.Member;
import com.example.schedulejpaapi.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<MemberSignUpResponseDto> signup(
            @Valid @RequestBody MemberSignupRequestDto requestDto,
            HttpServletRequest servletRequest
    ) {
        MemberSignUpResponseDto result = memberService.signUp(requestDto, servletRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // 로그인
    @GetMapping("/login")
    public ResponseEntity<MemberSignInResponseDto> login(
            @Valid @RequestBody MemberLoginRequestDto requestDto,
            HttpServletRequest servletRequest
    ) {
        MemberSignInResponseDto result = memberService.login(requestDto, servletRequest);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<MemberLogoutResponseDto> logout(HttpServletRequest request) {
        MemberLogoutResponseDto logoutMember = memberService.logout(request);
        return ResponseEntity.status(HttpStatus.OK).body(logoutMember);
    }

    // 회원 정보 변경
    @PatchMapping("/change")
    public ResponseEntity<MemberUpdateResponseDto> updateMember(
            @Valid @RequestBody MemberUpdateRequestDto requestDto,
            HttpServletRequest request
    ) {
        Optional<Member> loginMember = Optional.ofNullable((Member) request.getSession().getAttribute(Const.LOGIN_SESSION_KEY));
        MemberUpdateResponseDto memberUpdateResponseDto = memberService.updateMember(requestDto, loginMember);

        return ResponseEntity.status(HttpStatus.OK).body(memberUpdateResponseDto);
    }

    // 회원 탈퇴
    @DeleteMapping("/remove")
    public ResponseEntity<MemberRemoveResponseDto> removeMember(HttpServletRequest request) {
        MemberRemoveResponseDto removeMember = memberService.removeMember(request);
        return ResponseEntity.status(HttpStatus.OK).body(removeMember);
    }
}

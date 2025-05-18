package com.example.schedulejpaapi.controller;

import com.example.schedulejpaapi.constant.Const;
import com.example.schedulejpaapi.dto.*;
import com.example.schedulejpaapi.entity.Member;
import com.example.schedulejpaapi.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public ResponseEntity<MemberSignupResponseDto> signup(
            @Valid @RequestBody MemberSignupRequestDto requestDto,
            HttpServletRequest servletRequest
    ) {
        MemberSignupResponseDto result = memberService.signup(requestDto, servletRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // 로그인
    @GetMapping("/login")
    public ResponseEntity<MemberLoginResponseDto> login(
            @Valid @RequestBody MemberLoginRequestDto requestDto,
            HttpServletRequest servletRequest
    ) {
        MemberLoginResponseDto result = memberService.login(requestDto, servletRequest);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PatchMapping("/change")
    public ResponseEntity<MemberUpdateResponseDto> updateMember(
            @Valid @RequestBody MemberUpdateRequestDto requestDto,
            HttpServletRequest request
    ){
        Optional<Member> loginMember = Optional.ofNullable((Member) request.getSession().getAttribute(Const.LOGIN_SESSION_KEY));
        MemberUpdateResponseDto memberUpdateResponseDto = memberService.updateMember(requestDto, loginMember);

        return ResponseEntity.status(HttpStatus.OK).body(memberUpdateResponseDto);
    }

    @GetMapping("/logout")
    public ResponseEntity<MemberLogoutResponseDto> logout(HttpServletRequest request) {
        MemberLogoutResponseDto logoutMember = memberService.logout(request);
        return ResponseEntity.status(HttpStatus.OK).body(logoutMember);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<MemberRemoveResponseDto> removeMember(HttpServletRequest request) {
        MemberRemoveResponseDto removeMember = memberService.removeMember(request);
        return ResponseEntity.status(HttpStatus.OK).body(removeMember);
    }

}

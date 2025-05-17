package com.example.schedulejpaapi.controller;

import com.example.schedulejpaapi.constant.Const;
import com.example.schedulejpaapi.dto.MemberLoginRequestDto;
import com.example.schedulejpaapi.dto.MemberLoginResponseDto;
import com.example.schedulejpaapi.dto.MemberSignupRequestDto;
import com.example.schedulejpaapi.dto.MemberSignupResponseDto;
import com.example.schedulejpaapi.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            HttpServletRequest request
    ) {
        MemberSignupResponseDto result = memberService.signup(requestDto);

        HttpSession session = request.getSession();
        session.setAttribute(Const.LOGIN_SESSION_KEY, result);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/login")
    public ResponseEntity<MemberLoginResponseDto> login(
            @Valid @RequestBody MemberLoginRequestDto requestDto,
            HttpServletRequest request
    ) {
        MemberLoginResponseDto result = memberService.login(requestDto);

        HttpSession session = request.getSession();
        session.setAttribute(Const.LOGIN_SESSION_KEY, result);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}

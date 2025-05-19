package com.example.schedulejpaapi.controller;

import com.example.schedulejpaapi.dto.member.*;
import com.example.schedulejpaapi.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 회원 관련 API 요청을 처리하는 REST 컨트롤러
 * CRUD 기능을 제공
 */
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * 회원 생성
     *
     * @param requestDto     요청된 회원 정보 DTO{@link MemberSignUpRequestDto}
     * @param servletRequest HTTP 요청 객체
     * @return 상태코드와 생성된 회원 정보 DTO{@link MemberSignUpResponseDto}
     */
    @PostMapping("/signup")
    public ResponseEntity<MemberSignUpResponseDto> signup(
            @Valid @RequestBody MemberSignUpRequestDto requestDto,
            HttpServletRequest servletRequest
    ) {
        MemberSignUpResponseDto result = memberService.signUp(requestDto, servletRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * 로그인
     *
     * @param requestDto     요청된 회원 정보 DTO{@link MemberLoginRequestDto}
     * @param servletRequest HTTP 요청 객체
     * @return 상태코드와 로그인된 회원 정보 DTO{@link MemberLoginResponseDto}
     */
    @GetMapping("/login")
    public ResponseEntity<MemberLoginResponseDto> login(
            @Valid @RequestBody MemberLoginRequestDto requestDto,
            HttpServletRequest servletRequest
    ) {
        MemberLoginResponseDto result = memberService.loginByAccountOrEmail(requestDto, servletRequest);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 로그아웃
     *
     * @param servletRequest HTTP 요청 객체
     * @return 상태코드와 로그아웃한 회원 정보 DTO{@link MemberLogoutResponseDto}
     */
    @GetMapping("/logout")
    public ResponseEntity<MemberLogoutResponseDto> logout(HttpServletRequest servletRequest) {
        MemberLogoutResponseDto logoutMember = memberService.logout(servletRequest);

        return ResponseEntity.status(HttpStatus.OK).body(logoutMember);
    }

    /**
     * 회원 정보 변경
     *
     * @param requestDto     변경할 회원 정보 DTO{@link MemberUpdateRequestDto}
     * @param servletRequest HTTP 요청 객체
     * @return 변경된 회원 정보 DTO{@link MemberUpdateResponseDto}
     */
    @PatchMapping("/change")
    public ResponseEntity<MemberUpdateResponseDto> updateMember(
            @Valid @RequestBody MemberUpdateRequestDto requestDto,
            HttpServletRequest servletRequest
    ) {
        MemberUpdateResponseDto memberUpdateResponseDto = memberService.updateMember(requestDto, servletRequest);

        return ResponseEntity.status(HttpStatus.OK).body(memberUpdateResponseDto);
    }

    /**
     * 회원 탈퇴
     *
     * @param servletRequest HTTP 요청 객체
     * @return 삭제된 회원 정보 DTO{@link MemberRemoveResponseDto}
     */
    @DeleteMapping("/remove")
    public ResponseEntity<MemberRemoveResponseDto> removeMember(HttpServletRequest servletRequest) {
        MemberRemoveResponseDto removeMember = memberService.removeMember(servletRequest);

        return ResponseEntity.status(HttpStatus.OK).body(removeMember);
    }
}

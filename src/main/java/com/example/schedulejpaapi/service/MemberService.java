package com.example.schedulejpaapi.service;

import com.example.schedulejpaapi.config.PasswordEncoder;
import com.example.schedulejpaapi.config.Validator;
import com.example.schedulejpaapi.constant.Const;
import com.example.schedulejpaapi.dto.member.*;
import com.example.schedulejpaapi.entity.Member;
import com.example.schedulejpaapi.exceptions.custom.AlreadyAccountException;
import com.example.schedulejpaapi.exceptions.custom.IncorrectPasswordException;
import com.example.schedulejpaapi.exceptions.custom.UnauthorizedException;
import com.example.schedulejpaapi.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

// 회원 서비스
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, Validator validator) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }

    // 회원 가입
    @Transactional
    public MemberSignUpResponseDto signUp(MemberSignUpRequestDto requestDto, HttpServletRequest servletRequest) {
        Optional<Member> findMember = findMemberByAccountOrEmail(requestDto.getAccount(), requestDto.getEmail());
        if (findMember.isPresent()) {
            throw new AlreadyAccountException("Already Request");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        Member member = new Member(requestDto, encodedPassword);
        Member savedMember = memberRepository.save(member);
        setLoginSession(servletRequest, savedMember);

        return new MemberSignUpResponseDto(savedMember);
    }

    // 로그인
    @Transactional
    public MemberSignInResponseDto loginByAccountOrEmail(MemberLoginRequestDto requestDto, HttpServletRequest servletRequest) {
        Optional<Member> findMember = findMemberByAccountOrEmail(requestDto.getAccount(), requestDto.getEmail());
        Member loggedInMember = findMember.orElseThrow(() -> new UnauthorizedException("Unauthorized"));

        boolean pwMatch = passwordEncoder.matches(requestDto.getPassword(), loggedInMember.getPassword());
        if (!pwMatch) {
            throw new IncorrectPasswordException("incorrect password");
        }

        setLoginSession(servletRequest, loggedInMember);
        return new MemberSignInResponseDto(loggedInMember);
    }

    // 로그아웃
    @Transactional
    public MemberLogoutResponseDto logout(HttpServletRequest servletRequest) {
        Member member = validator.getLoggedInMember(servletRequest.getSession());

        invalidateSession(servletRequest);
        return new MemberLogoutResponseDto(member);
    }

    // 회원 정보 수정
    @Transactional
    public MemberUpdateResponseDto updateMember(MemberUpdateRequestDto requestDto, HttpServletRequest request) {
        Map<String, String> requestUpdateMap = requestDto.getUpdateMap();
        validator.verifyUpdatableField(requestUpdateMap, Const.UPDATE_MEMBER_FIELDS.keySet());

        Member loggedInMember = validator.getLoggedInMember(request.getSession());
        if(requestUpdateMap.containsKey("password")){
            requestUpdateMap.put("password", passwordEncoder.encode(requestUpdateMap.get("password")));
        }
        requestUpdateMap.forEach((field, value)
                -> Const.UPDATE_MEMBER_FIELDS.get(field).accept(loggedInMember, value));

        Member savedMember = memberRepository.save(loggedInMember);
        return new MemberUpdateResponseDto(savedMember);
    }

    // 회원 탈퇴
    @Transactional
    public MemberRemoveResponseDto removeMember(HttpServletRequest servletRequest) {
        Member member = validator.getLoggedInMember(servletRequest.getSession());

        invalidateSession(servletRequest);
        memberRepository.delete(member);
        return new MemberRemoveResponseDto(member);
    }

    // 아이디 또는 이메일로 탐색
    private Optional<Member> findMemberByAccountOrEmail(String account, String email) {
        Optional<Member> findMember = memberRepository.findByAccount(account);
        if (findMember.isEmpty()) {
            findMember = memberRepository.findByEmail(email);
        }
        return findMember;
    }

    // 쿠키+세션 추가
    private void setLoginSession(HttpServletRequest servletRequest, Member member) {
        HttpSession session = servletRequest.getSession();
        session.setAttribute(Const.LOGIN_SESSION_KEY, member);
    }

    // 쿠키+세션 무효화
    private void invalidateSession(HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession();
        session.invalidate();
    }
}

package com.example.schedulejpaapi.service;

import com.example.schedulejpaapi.config.PasswordEncoder;
import com.example.schedulejpaapi.constant.Const;
import com.example.schedulejpaapi.dto.member.*;
import com.example.schedulejpaapi.entity.Member;
import com.example.schedulejpaapi.exceptions.custom.AlreadyAccountException;
import com.example.schedulejpaapi.exceptions.custom.IncorrectPasswordException;
import com.example.schedulejpaapi.exceptions.custom.InvalidFieldException;
import com.example.schedulejpaapi.exceptions.custom.UnauthorizedException;
import com.example.schedulejpaapi.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

// 회원 서비스
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
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
        Member loggedInMember = getExistingMember(findMember);

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
        Member member = expireSessionAndGetMember(servletRequest);
        return new MemberLogoutResponseDto(member);
    }

    // 회원 정보 수정
    @Transactional
    public MemberUpdateResponseDto updateMember(MemberUpdateRequestDto requestDto, Optional<Member> loginMember) {
        Map<String, String> requestUpdateMap = requestDto.getUpdateMap();
        Member loggedInMember = getExistingMember(loginMember);

        Set<String> allowedFields = Const.UPDATE_MEMBER_FIELDS.keySet();
        Set<String> requestFields = requestUpdateMap.keySet();
        Set<String> invalidFields = requestFields.stream()
                .filter(field -> !allowedFields.contains(field))
                .collect(Collectors.toSet());
        if (!invalidFields.isEmpty()) {
            throw new InvalidFieldException("Invalid Field");
        }

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
        Member member = expireSessionAndGetMember(servletRequest);
        memberRepository.delete(member);
        return new MemberRemoveResponseDto(member);
    }

    // 회원 조회 검증
    private Member getExistingMember(Optional<Member> member) {
        return member.orElseThrow(() -> new UnauthorizedException("Unauthorized"));
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

    // 쿠키+세션 삭제
    private Member expireSessionAndGetMember(HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession();
        Member member = (Member) session.getAttribute(Const.LOGIN_SESSION_KEY);
        session.invalidate();

        return member;
    }
}

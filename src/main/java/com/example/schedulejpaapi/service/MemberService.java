package com.example.schedulejpaapi.service;

import com.example.schedulejpaapi.constant.Const;
import com.example.schedulejpaapi.dto.*;
import com.example.schedulejpaapi.entity.Member;
import com.example.schedulejpaapi.exceptions.custom.AlreadyAccountException;
import com.example.schedulejpaapi.exceptions.custom.InvalidFieldException;
import com.example.schedulejpaapi.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberSignupResponseDto signup(MemberSignupRequestDto requestDto, HttpServletRequest servletRequest) {
        Optional<Member> findMember =  memberRepository.findByAccount(requestDto.getAccount());
        Optional<Member> findEmail =  memberRepository.findByEmail(requestDto.getEmail());
        if(findMember.isPresent() || findEmail.isPresent()) {
            throw new AlreadyAccountException("Already Request");
        }

        Member member = new Member(requestDto);
        Member saveMember = memberRepository.save(member);
        setAttributeLoginSession(servletRequest, saveMember);

        return new MemberSignupResponseDto(saveMember);
    }

    public MemberLoginResponseDto login(MemberLoginRequestDto requestDto, HttpServletRequest servletRequest) {
        Optional<Member> findMember =  memberRepository.findByAccount(requestDto.getAccount());
        Member loginMember = validateMember(findMember);

        if(!loginMember.getPassword().equals(requestDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        setAttributeLoginSession(servletRequest, loginMember);

        return new MemberLoginResponseDto(loginMember);
    }

    public MemberUpdateResponseDto updateMember(MemberUpdateRequestDto requestDto, Optional<Member> loginMember) {
        Map<String, String> requestMap = requestDto.getUpdateMap();
        Member connectedMember = validateMember(loginMember);

        Set<String> allowedFields = Const.UPDATE_FIELDS.keySet();
        Set<String> requestFields = requestMap.keySet();
        Set<String> invalidFields = requestFields.stream()
                .filter(field -> !allowedFields.contains(field))
                .collect(Collectors.toSet());
        if(!invalidFields.isEmpty()){
            throw new InvalidFieldException("Invalid Field");
        }

        requestMap.forEach((field, value) -> Const.UPDATE_FIELDS.get(field).accept(connectedMember, value));

        Member saveMember = memberRepository.save(connectedMember);
        return new MemberUpdateResponseDto(saveMember);
    }

    public MemberLogoutResponseDto logout(HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession();
        Member member = (Member) session.getAttribute(Const.LOGIN_SESSION_KEY);
        session.invalidate();
        return new MemberLogoutResponseDto(member);
    }

    private <T> T validateMember(Optional<T> member) {
        return member.orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found account"));
    }

    private void setAttributeLoginSession(HttpServletRequest servletRequest, Member member) {
        HttpSession session = servletRequest.getSession();
        session.setAttribute(Const.LOGIN_SESSION_KEY, member);
    }
}

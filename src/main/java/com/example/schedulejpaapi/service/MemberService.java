package com.example.schedulejpaapi.service;

import com.example.schedulejpaapi.config.PasswordEncoder;
import com.example.schedulejpaapi.util.Validator;
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

/**
 * 회원 관련 비지니스 로직 서비스
 */
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;

    public MemberService(
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder,
            Validator validator
    ) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }

    /**
     * 새로운 회원 생성
     * 중복 계정 확인 및 비밀번호 암호화
     *
     * @param requestDto     가입할 정보 DTO{@link MemberSignUpRequestDto}}
     * @param servletRequest HTTP 요청 객체. 세션 정보 추출하여 사용
     * @return 생성된 회원 정보 DTO {@link MemberSignUpResponseDto}
     * @throws AlreadyAccountException 이미 존재하는 계정이면 발생
     */
    @Transactional
    public MemberSignUpResponseDto signUp(
            MemberSignUpRequestDto requestDto,
            HttpServletRequest servletRequest
    ) {
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

    /**
     * 계정 또는 이메일을 통한 로그인
     * 로그인 성공 시 세션설정
     *
     * @param requestDto     로그인 정보 DTO{@link MemberLoginRequestDto}}
     * @param servletRequest HTTP 요청 객체. 세션 정보 추출하여 사용
     * @return 로그인된 회원 정보 DTO {@link MemberLoginResponseDto}
     * @throws IncorrectPasswordException 비밀번호 틀릴 시 발 생
     */
    @Transactional
    public MemberLoginResponseDto loginByAccountOrEmail(
            MemberLoginRequestDto requestDto,
            HttpServletRequest servletRequest
    ) {
        Optional<Member> findMember = findMemberByAccountOrEmail(requestDto.getAccount(), requestDto.getEmail());
        Member loggedInMember = findMember.orElseThrow(() -> new UnauthorizedException("Unauthorized"));

        boolean pwMatch = passwordEncoder.matches(requestDto.getPassword(), loggedInMember.getPassword());
        if (!pwMatch) {
            throw new IncorrectPasswordException("incorrect password");
        }

        setLoginSession(servletRequest, loggedInMember);
        return new MemberLoginResponseDto(loggedInMember);
    }

    /**
     * 로그아웃
     * 로그인 상태면 로그아웃하고 세션 무효화
     *
     * @param servletRequest HTTP 요청 객체
     * @return 로그아웃된 회원의 정보 DTO {@link MemberLogoutResponseDto}
     */
    @Transactional
    public MemberLogoutResponseDto logout(Member loggedInMember, HttpServletRequest servletRequest) {
        invalidateSession(servletRequest);
        return new MemberLogoutResponseDto(loggedInMember);
    }

    /**
     * 회원 정보 수정
     *
     * @param requestDto     수정할 회원 정보 DTO{@link MemberUpdateRequestDto}}
     * @param servletRequest HTTP 요청 객체. 세션 정보 추출하여 사용
     * @return 수정된 회원 정보 DTO{@link MemberUpdateResponseDto}
     */
    @Transactional
    public MemberUpdateResponseDto updateMember(
            MemberUpdateRequestDto requestDto,
            HttpServletRequest servletRequest
    ) {
        Map<String, String> requestUpdateMap = requestDto.getUpdateMap();
        validator.verifyUpdatableField(requestUpdateMap, Const.UPDATE_MEMBER_FIELDS.keySet());

        if (requestUpdateMap.containsKey("password")) {
            requestUpdateMap.put("password", passwordEncoder.encode(requestUpdateMap.get("password")));
        }

        Member loggedInMember = getLoggedInMember(servletRequest.getSession());
        requestUpdateMap.forEach((field, value)
                -> Const.UPDATE_MEMBER_FIELDS.get(field).accept(loggedInMember, value));

        Member savedMember = memberRepository.save(loggedInMember);
        return new MemberUpdateResponseDto(savedMember);
    }

    /**
     * 회원 탈퇴
     *
     * @param servletRequest HTTP 요청 객체. 세션 정보 추출하여 사용
     * @return 탈퇴한 회원 정보 DTO{@link MemberRemoveResponseDto}
     */
    @Transactional
    public MemberRemoveResponseDto removeMember(Member loggedInMember, HttpServletRequest servletRequest) {
        invalidateSession(servletRequest);
        memberRepository.delete(loggedInMember);
        return new MemberRemoveResponseDto(loggedInMember);
    }

    /**
     * 계정 또는 이메일로 계정 탐색
     *
     * @param account 요청된 계정
     * @param email   요청된 이메일
     * @return 찾은 회원 정보를 담은 {@link Optional} 객체
     */
    private Optional<Member> findMemberByAccountOrEmail(String account, String email) {
        Optional<Member> findMember = memberRepository.findByAccount(account);
        if (findMember.isEmpty()) {
            findMember = memberRepository.findByEmail(email);
        }
        return findMember;
    }

    /**
     * 로그인 성공 시 세션에 저장
     *
     * @param servletRequest HTTP 요청 객체. 세션 정보 추출하여 사용
     * @param member         로그인된 Entity{@link Member}
     */
    private void setLoginSession(HttpServletRequest servletRequest, Member member) {
        HttpSession session = servletRequest.getSession();
        session.setAttribute(Const.LOGIN_SESSION_KEY, member);
    }

    /**
     * 현재 세션을 무효화
     *
     * @param servletRequest HTTP 요청 객체. 세션 정보 추출하여 사용
     */
    private void invalidateSession(HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession();
        session.invalidate();
    }

    /**
     * 세션에서 로그인된 정보 호출
     *
     * @param session 현 사용중인 세션
     * @return 로그인된 Entity {@link Member}
     * @throws UnauthorizedException 로그인된 정보가 없으면 발생
     */
    private Member getLoggedInMember(HttpSession session) {
        Optional<Member> loggedInMember
                = Optional.ofNullable((Member) session.getAttribute(Const.LOGIN_SESSION_KEY));
        if (loggedInMember.isEmpty()) throw new UnauthorizedException("Unauthorized");
        return loggedInMember.get();
    }
}

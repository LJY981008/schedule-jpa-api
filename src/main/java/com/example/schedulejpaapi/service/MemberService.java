package com.example.schedulejpaapi.service;

import com.example.schedulejpaapi.dto.MemberSignupRequestDto;
import com.example.schedulejpaapi.dto.MemberSignupResponseDto;
import com.example.schedulejpaapi.entity.Member;
import com.example.schedulejpaapi.exceptions.custom.AlreadyAccountException;
import com.example.schedulejpaapi.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberSignupResponseDto signup(MemberSignupRequestDto requestDto) {
        Optional<Member> findMember =  memberRepository.findByAccount(requestDto.getAccount());
        if(findMember.isPresent()) {
            throw new AlreadyAccountException("Already Request");
        }

        Member member = new Member(requestDto);
        Member saveMember = memberRepository.save(member);
        return new MemberSignupResponseDto(saveMember);
    }
}

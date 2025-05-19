package com.example.schedulejpaapi.repository;

import com.example.schedulejpaapi.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // id로 회원 조회
    Optional<Member> findByAccount(String account);

    // email로 회원 조회
    Optional<Member> findByEmail(String email);
}

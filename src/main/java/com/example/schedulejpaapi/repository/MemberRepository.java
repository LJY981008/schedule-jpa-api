package com.example.schedulejpaapi.repository;

import com.example.schedulejpaapi.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * {@link Member} Entity에 대한 데이터 접근을 제공하는 Repository 인터페이스
 * {@link JpaRepository}를 상속받아 CRUD 수행
 */
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    /**
     * 계정으로 회원 조회
     *
     * @param account 요청된 계정
     * @return 계정에 해당하는 Entity
     */
    Optional<Member> findByAccount(String account);

    /**
     * 이메일로 회원 조회
     *
     * @param email 요청된 이메일
     * @return 이메일에 해당하는 Entity
     */
    Optional<Member> findByEmail(String email);
}

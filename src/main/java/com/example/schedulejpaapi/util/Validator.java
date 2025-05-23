package com.example.schedulejpaapi.util;

import com.example.schedulejpaapi.entity.Member;
import com.example.schedulejpaapi.exception.custom.InvalidFieldException;
import com.example.schedulejpaapi.exception.custom.UnauthorizedException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 데이터 검증 유틸리티 클래스
 */
@Component
public class Validator {

    /**
     * 업데이트 요청의 Map이 수정 허용 필드인지 검증
     *
     * @param requestMap    수정 요청 Map
     * @param allowedFields 수정 허용 필드
     * @throws InvalidFieldException 허용하지 않은 필드가 하나라도 있으면 발생
     */
    public void verifyUpdatableField(Map<String, String> requestMap, Set<String> allowedFields) {
        Set<String> requestFields = requestMap.keySet();
        Set<String> invalidFields = requestFields.stream()
                .filter(field -> !allowedFields.contains(field))
                .collect(Collectors.toSet());
        if (!invalidFields.isEmpty()) {
            throw new InvalidFieldException("Invalid Field");
        }
    }

    /**
     * 작성자와 수정 요청자가 일치하는지 검증
     *
     * @param entity         검증할 Entity 객체
     * @param loggedInMember 현재 로그인한 {@link Member} 객체.(수정 요청자)
     * @param getAuthorIdFun Entity에서 작성자의 아이디를 가져오는 함수
     *                       ex) {@code entity -> entity.getMember().getId()}
     * @param <T>            검증할 Entity 타입
     * @throws UnauthorizedException 검증 실패 시 인증실패 예외 발생
     */
    public <T> void verifyAuthorOwner(T entity, Member loggedInMember, Function<T, Long> getAuthorIdFun) {
        Long loggedInMemberId = loggedInMember.getId();
        Long writerId = getAuthorIdFun.apply(entity);
        if (!loggedInMemberId.equals(writerId)) {
            throw new UnauthorizedException("Unauthorized");
        }
    }
}

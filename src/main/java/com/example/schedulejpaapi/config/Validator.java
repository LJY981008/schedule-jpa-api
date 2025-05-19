package com.example.schedulejpaapi.config;

import com.example.schedulejpaapi.constant.Const;
import com.example.schedulejpaapi.entity.Comment;
import com.example.schedulejpaapi.entity.Member;
import com.example.schedulejpaapi.entity.Post;
import com.example.schedulejpaapi.exceptions.custom.InvalidFieldException;
import com.example.schedulejpaapi.exceptions.custom.UnauthorizedException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class Validator {
    public void verifyUpdatableField(Map<String, String> requestMap, Set<String> allowedFields){
        Set<String> requestFields = requestMap.keySet();
        Set<String> invalidFields = requestFields.stream()
                .filter(field -> !allowedFields.contains(field))
                .collect(Collectors.toSet());
        if (!invalidFields.isEmpty()) {
            throw new InvalidFieldException("Invalid Field");
        }
    }

    public Member getLoggedInMember(HttpSession session) {
        Optional<Member> loggedInMember
                = Optional.ofNullable((Member) session.getAttribute(Const.LOGIN_SESSION_KEY));
        if (loggedInMember.isEmpty()) throw new UnauthorizedException("Unauthorized");
        return loggedInMember.get();
    }

    public <T>void verifyAuthorOwner(T entity, Member loggedInMember, Function<T, Long> getAuthorIdFun) {
        Long loggedInMemberId = loggedInMember.getId();
        Long writerId = getAuthorIdFun.apply(entity);
        if (!loggedInMemberId.equals(writerId)) {
            throw new UnauthorizedException("Unauthorized");
        }
    }
}

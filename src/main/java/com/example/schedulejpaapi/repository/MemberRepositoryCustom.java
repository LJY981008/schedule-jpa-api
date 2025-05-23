package com.example.schedulejpaapi.repository;

import java.util.Map;

public interface MemberRepositoryCustom {
    long updateMember(long memberId, Map<String, String> requestMap);
}

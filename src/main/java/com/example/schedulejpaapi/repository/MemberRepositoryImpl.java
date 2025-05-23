package com.example.schedulejpaapi.repository;

import com.example.schedulejpaapi.entity.QMember;
import com.example.schedulejpaapi.enums.MemberUpdateField;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public long updateMember(long memberId, Map<String, String> requestMap) {
        if(requestMap.isEmpty()) {
            throw new IllegalArgumentException("empty request content");
        }

        QMember member = QMember.member;
        JPAUpdateClause updateClause = queryFactory.update(member).where(member.id.eq(memberId));

        for(Map.Entry<String, String> entry : requestMap.entrySet()) {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue();

            Optional<MemberUpdateField> updateField = Optional.ofNullable(MemberUpdateField.fromFieldName(fieldName));
            if(updateField.isEmpty()) throw new IllegalArgumentException("illegal field name");

            Path<?> qPath = updateField.get().getQPath();
            updateClause.set((StringPath) qPath, fieldValue);
        }

        return updateClause.execute();
    }
}

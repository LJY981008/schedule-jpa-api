package com.example.schedulejpaapi.enums;

import com.example.schedulejpaapi.entity.QMember;
import com.querydsl.core.types.Path;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum MemberUpdateField {
    NAME("name", QMember.member.name),
    PASSWORD("password", QMember.member.password);

    private final String fieldName;
    private final Path<?> qPath;

    MemberUpdateField(String fieldName, Path<?> qPath) {
        this.fieldName = fieldName;
        this.qPath = qPath;
    }

    private static final Map<String, MemberUpdateField> map =
            Stream.of(values()).collect(Collectors.toMap(MemberUpdateField::getFieldName, Function.identity()));

    public static MemberUpdateField fromFieldName(String fieldName) {
        return map.get(fieldName);
    }
}

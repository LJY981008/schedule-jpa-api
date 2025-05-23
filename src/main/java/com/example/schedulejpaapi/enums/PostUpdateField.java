package com.example.schedulejpaapi.enums;

import com.example.schedulejpaapi.entity.QPost;
import com.querydsl.core.types.Path;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum PostUpdateField {
    TITLE("title", QPost.post.title),
    CONTENTS("contents", QPost.post.contents),
    MODIFIED_AT("modifiedAt", QPost.post.modifiedAt);

    private final String fieldName;
    private final Path<?> qPath;

    PostUpdateField(String fieldName, Path<?> qPath) {
        this.fieldName = fieldName;
        this.qPath = qPath;
    }

    private static final Map<String, PostUpdateField> map =
            Stream.of(values()).collect(Collectors.toMap(PostUpdateField::getFieldName, Function.identity()));

    public static PostUpdateField fromFieldName(String fieldName) {
        return map.get(fieldName);
    }
}

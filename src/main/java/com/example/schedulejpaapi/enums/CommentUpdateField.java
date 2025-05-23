package com.example.schedulejpaapi.enums;

import com.example.schedulejpaapi.entity.QComment;
import com.example.schedulejpaapi.entity.QPost;
import com.querydsl.core.types.Path;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum CommentUpdateField {
    TITLE("contents", QComment.comment.contents);

    private final String fieldName;
    private final Path<?> qPath;

    CommentUpdateField(String fieldName, Path<?> qPath) {
        this.fieldName = fieldName;
        this.qPath = qPath;
    }

    private static final Map<String, CommentUpdateField> map =
            Stream.of(values()).collect(Collectors.toMap(CommentUpdateField::getFieldName, Function.identity()));

    public static CommentUpdateField fromFieldName(String fieldName) {
        return map.get(fieldName);
    }
}

package com.example.schedulejpaapi.constant;

import com.example.schedulejpaapi.entity.Comment;
import com.example.schedulejpaapi.entity.Member;
import com.example.schedulejpaapi.entity.Post;

import java.util.Map;
import java.util.function.BiConsumer;

/**
 * API에서 사용하는 상수 정의
 */
public class Const {

    /**
     * 세션
     */
    public static final String LOGIN_SESSION_KEY = "LOGIN_SESSION_KEYJKWQUR#GK@BK!$";
    public static final Map<String, BiConsumer<Member, String>> UPDATE_MEMBER_FIELDS = Map.of(
            "name", Member::updateName,
            "password", Member::updatePassword
    );

    public static final Map<String, BiConsumer<Post, String>> UPDATE_POST_FIELDS = Map.of(
            "title", Post::updateTitle,
            "contents", Post::updateContents
    );

    public static final Map<String, BiConsumer<Comment, String>> UPDATE_COMMENT_FIELDS = Map.of(
            "contents", Comment::updateContents
    );

    private Const() {
    }
}

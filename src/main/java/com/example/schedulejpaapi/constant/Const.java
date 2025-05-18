package com.example.schedulejpaapi.constant;

import com.example.schedulejpaapi.entity.Member;
import com.example.schedulejpaapi.entity.Post;

import java.util.Map;
import java.util.function.BiConsumer;

public class Const {

    public static final String LOGIN_SESSION_KEY = "MyAPILoginKey";

    public static final Map<String, BiConsumer<Member, String>> UPDATE_MEMBER_FIELDS = Map.of(
            "name", Member::updateName,
            "password", Member::updatePassword
    );

    public static final Map<String, BiConsumer<Post, String>> UPDATE_POST_FIELDS = Map.of(
            "title", Post::updateTitle,
            "contents", Post::updateContents
    );

    private Const() {
    }
}

package com.example.schedulejpaapi.constant;

import com.example.schedulejpaapi.entity.Member;

import java.util.Map;
import java.util.function.BiConsumer;

public class Const {

    public static final String LOGIN_SESSION_KEY = "MyAPILoginKey";

    public static final Map<String, BiConsumer<Member, String>> UPDATE_FIELDS
            = Map.of("name", Member::updateName, "password", Member::updatePassword);

    private Const() {
    }
}

package com.example.schedulejpaapi.repository;

import java.util.Map;

public interface CommentRepositoryCustom {
    long updateComment(long commentId, Map<String, String> requestMap);
}

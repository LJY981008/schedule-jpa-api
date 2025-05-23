package com.example.schedulejpaapi.repository.comment;

import java.util.Map;

public interface CommentRepositoryCustom {
    long updateComment(long commentId, Map<String, String> requestMap);
}

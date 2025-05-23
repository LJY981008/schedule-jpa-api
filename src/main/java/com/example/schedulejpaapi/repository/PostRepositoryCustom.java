package com.example.schedulejpaapi.repository;

import java.util.Map;

public interface PostRepositoryCustom {
    long updatePost(Long postId, Map<String, String> requestMap);
}

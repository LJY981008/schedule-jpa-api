package com.example.schedulejpaapi.repository.post;

import java.util.Map;

public interface PostRepositoryCustom {
    long updatePost(Long postId, Map<String, String> requestMap);
}

package com.example.schedulejpaapi.repository;

import java.util.Map;

public interface PostRepositoryCustom {
    long updatePostOfTitleOrContents(Long postId, Map<String, String> requestMap);
}

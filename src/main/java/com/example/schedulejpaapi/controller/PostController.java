package com.example.schedulejpaapi.controller;

import com.example.schedulejpaapi.dto.post.PostCreateRequestDto;
import com.example.schedulejpaapi.dto.post.PostCreateResponseDto;
import com.example.schedulejpaapi.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    public ResponseEntity<PostCreateResponseDto> createPost(
            @Valid @RequestBody PostCreateRequestDto requestDto,
            HttpServletRequest servletRequest
    ){
        PostCreateResponseDto post = postService.createPost(requestDto, servletRequest);
        return ResponseEntity.ok().body(post);
    }
}

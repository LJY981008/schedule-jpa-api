package com.example.schedulejpaapi.controller;

import com.example.schedulejpaapi.dto.post.*;
import com.example.schedulejpaapi.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @GetMapping("/find")
    public ResponseEntity<PostFindResponseDto> getPostById(@RequestParam Long postId){
        PostFindResponseDto post = postService.getPostById(postId);
        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostFindResponseDto>> getPosts(){
        List<PostFindResponseDto> posts = postService.getPosts();
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @PatchMapping("/change")
    public ResponseEntity<PostUpdateResponseDto> updateMember(
            @RequestParam Long postId,
            @Valid @RequestBody PostUpdateRequestDto requestDto,
            HttpServletRequest request
    ){
        PostUpdateResponseDto postUpdateResponseDto = postService.updatePost(postId, requestDto, request);
        return ResponseEntity.status(HttpStatus.OK).body(postUpdateResponseDto);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<PostRemoveResponseDto> removeMember(
            @RequestParam Long postId,
            HttpServletRequest request
    ){
        PostRemoveResponseDto postRemoveResponseDto = postService.removePost(postId, request);
        return ResponseEntity.status(HttpStatus.OK).body(postRemoveResponseDto);
    }
}

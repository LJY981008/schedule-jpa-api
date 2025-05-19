package com.example.schedulejpaapi.controller;

import com.example.schedulejpaapi.dto.comment.*;
import com.example.schedulejpaapi.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 선택한 스케줄에 댓글 생성
    @PostMapping("/create")
    public ResponseEntity<CommentCreateResponseDto> createComment(
            @Valid @RequestBody CommentCreateRequestDto requestDto,
            @RequestParam Long postId,
            HttpServletRequest servletRequest
    ) {
        CommentCreateResponseDto comment = commentService.createComment(requestDto, postId, servletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    // 선택한 스케줄의 댓글 전체 조회
    @GetMapping
    public ResponseEntity<List<CommentFindByPostResponseDto>> getCommentsByPostId(
            @RequestParam Long postId
    ) {
        List<CommentFindByPostResponseDto> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    // 댓글 수정
    @PatchMapping("/change")
    public ResponseEntity<CommentUpdateResponseDto> updateComment(
            @RequestParam Long commentId,
            @Valid @RequestBody CommentUpdateRequestDto requestDto,
            HttpServletRequest request
    ) {
        CommentUpdateResponseDto comment = commentService.updateComment(commentId, requestDto, request);
        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<CommentRemoveResponseDto> removeComment(
            @RequestParam Long commentId
    ) {
        CommentRemoveResponseDto comment = commentService.removeComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }
}

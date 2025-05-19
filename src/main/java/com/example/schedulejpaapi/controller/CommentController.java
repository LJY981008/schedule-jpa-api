package com.example.schedulejpaapi.controller;

import com.example.schedulejpaapi.dto.comment.CommentCreateRequestDto;
import com.example.schedulejpaapi.dto.comment.CommentCreateResponseDto;
import com.example.schedulejpaapi.dto.comment.CommentFindByPostResponseDto;
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

    // 댓글 생성
    @PostMapping("/create")
    public ResponseEntity<CommentCreateResponseDto> createComment(
            @Valid @RequestBody CommentCreateRequestDto requestDto,
            @RequestParam Long postId,
            HttpServletRequest servletRequest
    ) {
        CommentCreateResponseDto comment = commentService.createComment(requestDto, postId, servletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    // 선택한 스케줄의 댓글

    @GetMapping
    public ResponseEntity<List<CommentFindByPostResponseDto>> getCommentsByPostId(
            @RequestParam Long postId
    ) {
        List<CommentFindByPostResponseDto> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }
}

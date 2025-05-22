package com.example.schedulejpaapi.controller;

import com.example.schedulejpaapi.dto.comment.*;
import com.example.schedulejpaapi.entity.Member;
import com.example.schedulejpaapi.service.CommentService;
import com.example.schedulejpaapi.config.authresolver.MemberAuth;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 댓글 관련 API 요청을 처리하는 REST 컨트롤러
 * CRUD 기능을 제공
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 댓글 생성
     *
     * @param requestDto     생성 요청된 댓글 DTO{@link CommentCreateRequestDto}
     * @param postId         댓글이 등록될 스케줄 ID
     * @param loggedInMember 로그인한 멤버 정보{@link Member}
     * @return 생성된 댓글 정보 DTO{@link CommentCreateResponseDto}
     */
    @PostMapping("/create")
    public ResponseEntity<CommentCreateResponseDto> createComment(
            @RequestParam Long postId,
            @Valid @RequestBody CommentCreateRequestDto requestDto,
            @MemberAuth Member loggedInMember
    ) {
        CommentCreateResponseDto comment = commentService.createComment(postId, requestDto, loggedInMember);

        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    /**
     * 특정 스케줄에 등록된 댓글 목록
     *
     * @param postId 조회할 스케줄 ID
     * @return 조회된 댓글 정보 DTO{@link CommentFindByPostResponseDto} 리스트
     */
    @GetMapping
    public ResponseEntity<List<CommentFindByPostResponseDto>> getCommentsByPostId(@RequestParam Long postId) {
        List<CommentFindByPostResponseDto> comments = commentService.getCommentsByPostId(postId);

        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    /**
     * 댓글 수정
     *
     * @param commentId      수정할 댓글 ID
     * @param requestDto     수정 정보 DTO{@link CommentUpdateRequestDto}
     * @param loggedInMember 로그인한 멤버 정보{@link Member}
     * @return 수정된 댓글 정보 DTO{@link CommentUpdateResponseDto}
     */
    @PatchMapping("/change")
    public ResponseEntity<CommentUpdateResponseDto> updateComment(
            @RequestParam Long commentId,
            @Valid @RequestBody CommentUpdateRequestDto requestDto,
            @MemberAuth Member loggedInMember
    ) {
        CommentUpdateResponseDto comment = commentService.updateComment(commentId, requestDto, loggedInMember);

        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }

    /**
     * 댓글 삭제
     *
     * @param commentId         삭제할 댓글 ID
     * @param loggedInMember    로그인한 멤버 정보{@link Member}
     * @return 삭제된 댓글 정보 DTO{@link CommentRemoveResponseDto}
     */
    @DeleteMapping("/remove")
    public ResponseEntity<CommentRemoveResponseDto> removeComment(
            @RequestParam Long commentId,
            @MemberAuth Member loggedInMember
    ) {
        CommentRemoveResponseDto comment = commentService.removeComment(commentId, loggedInMember);

        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }
}

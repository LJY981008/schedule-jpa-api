package com.example.schedulejpaapi.controller;

import com.example.schedulejpaapi.dto.post.*;
import com.example.schedulejpaapi.entity.Member;
import com.example.schedulejpaapi.service.PostService;
import com.example.schedulejpaapi.config.authresolver.MemberAuth;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 스케줄 관련 API 요청을 처리하는 REST 컨트롤러
 * CRUD 기능을 제공
 */
@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * 스케줄 생성
     *
     * @param requestDto     요청 정보 DTO{@link PostCreateRequestDto}
     * @param loggedInMember 로그인한 멤버 정보{@link Member}
     * @return 상태코드와 응답 DTO{@link PostCreateResponseDto}
     */
    @PostMapping("/create")
    public ResponseEntity<PostCreateResponseDto> createPost(
            @Valid @RequestBody PostCreateRequestDto requestDto,
            @MemberAuth Member loggedInMember
            ) {
        PostCreateResponseDto post = postService.createPost(requestDto, loggedInMember);

        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    /**
     * 특정 스케줄 조회
     *
     * @param postId 조회할 스케줄 ID
     * @return 상태코드와 조회된 스케줄 DTO{@link PostFindResponseDto}
     */
    @GetMapping("/find")
    public ResponseEntity<PostFindResponseDto> getPostById(@RequestParam Long postId) {
        PostFindResponseDto post = postService.getPostById(postId);

        return ResponseEntity.status(HttpStatus.OK).body(post);
    }

    /**
     * 스케줄 목록을 페이지네이션을 적용해 조회
     *
     * @param page 조회할 페이지 (기본값 : 0)
     * @param size 한 페이지에 조회될 크기 (기본값 : 10)
     * @return 상태코드와 스케줄 목록 DTO{@link PostFindAllResponseDto} 리스트
     */
    @GetMapping("/all")
    public ResponseEntity<List<PostFindAllResponseDto>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<PostFindAllResponseDto> posts = postService.getPosts(page, size);

        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    /**
     * 특정 스케줄의 내용 수정
     *
     * @param postId         수정할 스케줄 ID
     * @param requestDto     요청된 수정 정보 DTO{@link PostUpdateRequestDto}
     * @param loggedInMember 로그인한 멤버 정보{@link Member}
     * @return 수정된 스케줄 정보 DTO{@link PostFindResponseDto}
     */
    @PatchMapping("/change")
    public ResponseEntity<PostFindResponseDto> updateMember(
            @RequestParam Long postId,
            @Valid @RequestBody PostUpdateRequestDto requestDto,
            @MemberAuth Member loggedInMember
    ) {
        postService.updatePost(postId, requestDto, loggedInMember);
        PostFindResponseDto postById = postService.getPostById(postId);
        return ResponseEntity.status(HttpStatus.OK).body(postById);
    }

    /**
     * 스케줄 삭제
     *
     * @param postId         삭제할 스케줄 ID
     * @param loggedInMember 로그인한 멤버 정보{@link Member}
     * @return 상태 코드와 삭제된 스케줄 정보 DTO{@link PostRemoveResponseDto}
     */
    @DeleteMapping("/remove")
    public ResponseEntity<PostRemoveResponseDto> removeMember(
            @RequestParam Long postId,
            @MemberAuth Member loggedInMember
    ) {
        PostRemoveResponseDto postRemoveResponseDto = postService.removePost(postId, loggedInMember);

        return ResponseEntity.status(HttpStatus.OK).body(postRemoveResponseDto);
    }
}

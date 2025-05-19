package com.example.schedulejpaapi.service;

import com.example.schedulejpaapi.config.Validator;
import com.example.schedulejpaapi.constant.Const;
import com.example.schedulejpaapi.dto.post.*;
import com.example.schedulejpaapi.entity.Member;
import com.example.schedulejpaapi.entity.Post;
import com.example.schedulejpaapi.exceptions.custom.NotFoundPostException;
import com.example.schedulejpaapi.repository.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

// 스케줄 서비스
@Service
public class PostService {

    private final PostRepository postRepository;
    private final Validator validator;

    public PostService(PostRepository postRepository, Validator validator) {
        this.postRepository = postRepository;
        this.validator = validator;
    }

    // 스케줄 생성
    @Transactional
    public PostCreateResponseDto createPost(PostCreateRequestDto requestDto, HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession();
        Member writer = validator.getLoggedInMember(session);

        Post postSetWriter = new Post(requestDto, writer);
        Post savedPost = postRepository.save(postSetWriter);

        return new PostCreateResponseDto(savedPost);
    }

    // 스케줄 단건조회
    @Transactional
    public PostFindResponseDto getPostById(Long postId) {
        Post findPost = getExistingPost(postId);
        return new PostFindResponseDto(findPost);
    }

    // 스케줄 전체조회
    @Transactional(readOnly = true)
    public List<PostFindAllResponseDto> getPosts(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "modifiedAt");
        Pageable pageable = PageRequest.of(page, size, sort);


        List<Post> findPosts = postRepository.findAll(pageable).getContent();
        if (findPosts.isEmpty()) throw new NotFoundPostException("NotFound Post");
        return findPosts.stream().map(PostFindAllResponseDto::new).toList();
    }

    // 스케줄 수정
    @Transactional
    public PostUpdateResponseDto updatePost(Long postId, PostUpdateRequestDto requestDto, HttpServletRequest request) {
        Post findPost = verifyPostAccess(postId, request);

        Map<String, String> requestUpdateMap = requestDto.getUpdateMap();
        validator.verifyUpdatableField(requestUpdateMap, Const.UPDATE_POST_FIELDS.keySet());

        requestUpdateMap.forEach((field, value)
                -> Const.UPDATE_POST_FIELDS.get(field).accept(findPost, value));
        Post savedPost = postRepository.save(findPost);

        return new PostUpdateResponseDto(savedPost);
    }

    // 스케줄 삭제
    @Transactional
    public PostRemoveResponseDto removePost(Long postId, HttpServletRequest request) {
        Post findPost = verifyPostAccess(postId, request);

        findPost.getMember().getPosts().remove(findPost);
        postRepository.delete(findPost);

        return new PostRemoveResponseDto(findPost);
    }

    // 로그인 검증, 게시글 탐색 후 검증, 작성자 일치 검증
    private Post verifyPostAccess(Long postId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Member loggedInMember = validator.getLoggedInMember(session);
        Post findPost = getExistingPost(postId);
        validator.verifyAuthorOwner(findPost, loggedInMember,
                (post) -> post.getMember().getId());

        return findPost;
    }

    // 스케줄 탐색 후 검증
    private Post getExistingPost(Long postId) {
        Optional<Post> findPost = postRepository.findById(postId);
        if (findPost.isEmpty()) throw new NotFoundPostException("NotFound Post");
        return findPost.get();
    }

}

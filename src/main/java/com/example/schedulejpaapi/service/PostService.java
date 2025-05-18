package com.example.schedulejpaapi.service;

import com.example.schedulejpaapi.constant.Const;
import com.example.schedulejpaapi.dto.post.*;
import com.example.schedulejpaapi.entity.Member;
import com.example.schedulejpaapi.entity.Post;
import com.example.schedulejpaapi.exceptions.custom.InvalidFieldException;
import com.example.schedulejpaapi.exceptions.custom.NotFoundPostException;
import com.example.schedulejpaapi.exceptions.custom.UnauthorizedException;
import com.example.schedulejpaapi.repository.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 스케줄 생성
    @Transactional
    public PostCreateResponseDto createPost(PostCreateRequestDto requestDto, HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession();
        Member writer = getLoggedInMember(session);;

        Post postSetWriter = new Post(requestDto, writer);
        Post savedPost = postRepository.save(postSetWriter);
        return new PostCreateResponseDto(savedPost);
    }

    // 스케줄 단건조회
    @Transactional(readOnly = true)
    public PostFindResponseDto getPostById(Long postId) {
        Post findPost = getExistingPost(postId);
        return new PostFindResponseDto(findPost);
    }

    // 스케줄 전체조회
    @Transactional(readOnly = true)
    public List<PostFindResponseDto> getPosts() {
        List<Post> findPosts = postRepository.findAll();
        if (findPosts.isEmpty()) throw new NotFoundPostException("NotFound Post");
        return findPosts.stream().map(PostFindResponseDto::new).toList();
    }

    // 스케줄 수정
    @Transactional
    public PostUpdateResponseDto updatePost(Long postId, PostUpdateRequestDto requestDto, HttpServletRequest request) {
        Post findPost = verifyPostAccess(postId, request);
        // 수정 요청 필드 검증
        Map<String, String> requestUpdateMap = requestDto.getUpdateMap();
        verifyUpdatableField(requestUpdateMap);

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
        Member loggedInMember = getLoggedInMember(session);
        Post findPost = getExistingPost(postId);
        verifyPostAuthor(findPost, loggedInMember);

        return findPost;
    }

    // 현재 로그인 멤버 검증
    private Member getLoggedInMember(HttpSession session) {
        Optional<Member> loggedInMember
                = Optional.ofNullable((Member) session.getAttribute(Const.LOGIN_SESSION_KEY));
        if (loggedInMember.isEmpty()) throw new UnauthorizedException("Unauthorized");
        return loggedInMember.get();
    }

    // 스케줄 탐색 후 검증
    private Post getExistingPost(Long postId) {
        Optional<Post> findPost = postRepository.findById(postId);
        if (findPost.isEmpty()) throw new NotFoundPostException("NotFound Post");
        return findPost.get();
    }

    // 작성자 일치 검증
    private void verifyPostAuthor(Post post, Member loggedInMember) {
        Long loggedInMemberId = loggedInMember.getId();
        Long postWriterId = post.getMember().getId();
        if (!loggedInMemberId.equals(postWriterId)) {
            throw new UnauthorizedException("Unauthorized");
        }
    }

    // 수정 요청 필드 검증
    private void verifyUpdatableField(Map<String, String> requestMap){
        Set<String> allowedFields = Const.UPDATE_POST_FIELDS.keySet();
        Set<String> requestFields = requestMap.keySet();
        Set<String> invalidFields = requestFields.stream()
                .filter(field -> !allowedFields.contains(field))
                .collect(Collectors.toSet());
        if (!invalidFields.isEmpty()) {
            throw new InvalidFieldException("Invalid Field");
        }
    }
}

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
import jakarta.validation.Valid;
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
        Optional<Member> writer = Optional.ofNullable((Member) session.getAttribute(Const.LOGIN_SESSION_KEY));
        if(writer.isEmpty()) throw new UnauthorizedException("Unauthorized");
        Member verifiedWriter = writer.get();

        Post post = new Post(requestDto, verifiedWriter);
        Post savePost = postRepository.save(post);
        return new PostCreateResponseDto(savePost);
    }

    public PostFindResponseDto getPostById(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty()) throw new NotFoundPostException("NotFound Post");
        Post findPost = post.get();
        return new PostFindResponseDto(findPost);
    }

    public List<PostFindResponseDto> getPosts() {
        List<Post> posts = postRepository.findAll();
        if(posts.isEmpty()) throw new NotFoundPostException("NotFound Post");
        return posts.stream()
                .map(PostFindResponseDto::new)
                .toList();
    }

    public PostUpdateResponseDto updatePost(
            Long postId,
            PostUpdateRequestDto requestDto,
            HttpServletRequest request
    ) {
        HttpSession session = request.getSession();
        Optional<Member> loginMemberResult = Optional.ofNullable((Member) session.getAttribute(Const.LOGIN_SESSION_KEY));
        if(loginMemberResult.isEmpty()) throw new UnauthorizedException("Unauthorized");
        Optional<Post> currentPostResult = postRepository.findById(postId);
        if(currentPostResult.isEmpty()) throw new NotFoundPostException("NotFound Post");

        Map<String, String> requestMap = requestDto.getUpdateMap();

        Set<String> allowedFields = Const.UPDATE_POST_FIELDS.keySet();
        Set<String> requestFields = requestMap.keySet();
        Set<String> invalidFields = requestFields.stream()
                .filter(field -> !allowedFields.contains(field))
                .collect(Collectors.toSet());
        if(!invalidFields.isEmpty()){
            throw new InvalidFieldException("Invalid Field");
        }

        Member loginMemberEntity = loginMemberResult.get();
        Post currentPostEntity = currentPostResult.get();

        Long loginMemberId = loginMemberEntity.getId();
        Long currentPostWriterId = currentPostEntity.getMember().getId();

        if(!loginMemberId.equals(currentPostWriterId)) {
            throw new UnauthorizedException("Unauthorized");
        }

        requestMap.forEach((field, value) -> Const.UPDATE_POST_FIELDS.get(field).accept(currentPostEntity, value));
        Post savePost = postRepository.save(currentPostEntity);

        return new PostUpdateResponseDto(savePost);
    }

    public PostRemoveResponseDto removePost(Long postId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Optional<Member> loginMemberResult = Optional.ofNullable((Member) session.getAttribute(Const.LOGIN_SESSION_KEY));
        if(loginMemberResult.isEmpty()) throw new UnauthorizedException("Unauthorized");
        Optional<Post> currentPostResult = postRepository.findById(postId);
        if(currentPostResult.isEmpty()) throw new NotFoundPostException("NotFound Post");

        Member loginMemberEntity = loginMemberResult.get();
        Post currentPostEntity = currentPostResult.get();

        Long loginMemberId = loginMemberEntity.getId();
        Long currentPostWriterId = currentPostEntity.getMember().getId();

        if(!loginMemberId.equals(currentPostWriterId)) {
            throw new UnauthorizedException("Unauthorized");
        }

        postRepository.delete(currentPostEntity);
        loginMemberEntity.getPost().remove(currentPostEntity);
        return new PostRemoveResponseDto(currentPostEntity);
    }
}

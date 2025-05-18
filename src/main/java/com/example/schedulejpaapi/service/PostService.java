package com.example.schedulejpaapi.service;

import com.example.schedulejpaapi.constant.Const;
import com.example.schedulejpaapi.dto.post.PostCreateRequestDto;
import com.example.schedulejpaapi.dto.post.PostCreateResponseDto;
import com.example.schedulejpaapi.dto.post.PostFindResponseDto;
import com.example.schedulejpaapi.entity.Member;
import com.example.schedulejpaapi.entity.Post;
import com.example.schedulejpaapi.exceptions.custom.NotFoundPostException;
import com.example.schedulejpaapi.exceptions.custom.UnauthorizedException;
import com.example.schedulejpaapi.repository.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
}

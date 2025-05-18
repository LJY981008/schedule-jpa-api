package com.example.schedulejpaapi.service;

import com.example.schedulejpaapi.constant.Const;
import com.example.schedulejpaapi.dto.post.PostCreateRequestDto;
import com.example.schedulejpaapi.dto.post.PostCreateResponseDto;
import com.example.schedulejpaapi.entity.Member;
import com.example.schedulejpaapi.entity.Post;
import com.example.schedulejpaapi.exceptions.custom.UnauthorizedException;
import com.example.schedulejpaapi.repository.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

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
}

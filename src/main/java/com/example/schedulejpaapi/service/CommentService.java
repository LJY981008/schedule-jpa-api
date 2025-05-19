package com.example.schedulejpaapi.service;

import com.example.schedulejpaapi.constant.Const;
import com.example.schedulejpaapi.dto.comment.CommentCreateRequestDto;
import com.example.schedulejpaapi.dto.comment.CommentCreateResponseDto;
import com.example.schedulejpaapi.dto.comment.CommentFindByPostResponseDto;
import com.example.schedulejpaapi.entity.Comment;
import com.example.schedulejpaapi.entity.Member;
import com.example.schedulejpaapi.entity.Post;
import com.example.schedulejpaapi.exceptions.custom.NotFoundPostException;
import com.example.schedulejpaapi.exceptions.custom.UnauthorizedException;
import com.example.schedulejpaapi.repository.CommentRepository;
import com.example.schedulejpaapi.repository.PostRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public CommentCreateResponseDto createComment(
            CommentCreateRequestDto requestDto,
            Long postId,
            HttpServletRequest servletRequest
    ) {
        Post selectPost = getCommentByPost(postId);
        Member loggedInMember = getLoggedInMember(servletRequest.getSession());
        Comment comment = new Comment(requestDto, selectPost, loggedInMember);

        Comment savedComment = commentRepository.save(comment);
        return new CommentCreateResponseDto(savedComment);
    }

    public List<CommentFindByPostResponseDto> getCommentsByPostId(Long postId) {
        Post selectPost = getCommentByPost(postId);
        List<Comment> comments = getCommentListByPost(selectPost);

        return comments.stream()
                .map(CommentFindByPostResponseDto::new)
                .toList();
    }

    private Member getLoggedInMember(HttpSession session) {
        Optional<Member> loggedInMember
                = Optional.ofNullable((Member) session.getAttribute(Const.LOGIN_SESSION_KEY));
        if (loggedInMember.isEmpty()) throw new UnauthorizedException("Unauthorized");
        return loggedInMember.get();
    }

    private Post getCommentByPost(Long postId){
        Optional<Post> selectedPost = postRepository.findById(postId);
        if(selectedPost.isEmpty()) throw new NotFoundPostException("NotFound Post");
        return selectedPost.get();
    }

    private List<Comment> getCommentListByPost(Post post){
        List<Comment> comments = commentRepository.findByPost(post);
        return comments.isEmpty() ? List.of() : comments;
    }


}

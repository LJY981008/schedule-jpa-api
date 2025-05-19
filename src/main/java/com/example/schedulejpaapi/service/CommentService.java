package com.example.schedulejpaapi.service;

import com.example.schedulejpaapi.config.Validator;
import com.example.schedulejpaapi.constant.Const;
import com.example.schedulejpaapi.dto.comment.*;
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
import java.util.Map;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final Validator validator;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, Validator validator) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.validator = validator;
    }

    public CommentCreateResponseDto createComment(
            CommentCreateRequestDto requestDto,
            Long postId,
            HttpServletRequest servletRequest
    ) {
        Post selectPost = getCommentByPost(postId);
        Member loggedInMember = validator.getLoggedInMember(servletRequest.getSession());
        Comment comment = new Comment(requestDto, selectPost, loggedInMember);

        Comment savedComment = commentRepository.save(comment);
        return new CommentCreateResponseDto(savedComment);
    }

    public List<CommentFindByPostResponseDto> getCommentsByPostId(Long postId) {
        Post selectPost = getCommentByPost(postId);
        List<Comment> comments = commentRepository.findByPost(selectPost);

        return comments.stream()
                .map(CommentFindByPostResponseDto::new)
                .toList();
    }

    // 댓글 수정
    public CommentUpdateResponseDto updateComment(
            Long commentId,
            CommentUpdateRequestDto requestDto,
            HttpServletRequest request
    ) {
        Member loggedInMember = validator.getLoggedInMember(request.getSession());
        Comment selectComment = getCommentById(commentId);
        validator.verifyAuthorOwner(selectComment, loggedInMember,
                (comment) -> comment.getPost().getId());

        Map<String, String> requestUpdateMap = requestDto.getUpdateMap();
        validator.verifyUpdatableField(requestUpdateMap, Const.UPDATE_COMMENT_FIELDS.keySet());

        requestUpdateMap.forEach((field, value)
                -> Const.UPDATE_COMMENT_FIELDS.get(field).accept(selectComment, value));
        Comment savedComment = commentRepository.save(selectComment);

        return new CommentUpdateResponseDto(savedComment);
    }

    public CommentRemoveResponseDto removeComment(Long commentId) {
        Comment selectComment = getCommentById(commentId);
        commentRepository.delete(selectComment);
        return new CommentRemoveResponseDto(selectComment);
    }

    private Post getCommentByPost(Long postId){
        Optional<Post> selectedPost = postRepository.findById(postId);
        if(selectedPost.isEmpty()) throw new NotFoundPostException("NotFound Post");
        return selectedPost.get();
    }

    private Comment getCommentById(Long commentId){
        Optional<Comment> selectedComment = commentRepository.findById(commentId);
        if(selectedComment.isEmpty()) throw new NotFoundPostException("NotFound Comment");
        return selectedComment.get();
    }
}

package com.example.schedulejpaapi.service;

import com.example.schedulejpaapi.constant.Const;
import com.example.schedulejpaapi.dto.comment.*;
import com.example.schedulejpaapi.entity.Comment;
import com.example.schedulejpaapi.entity.Member;
import com.example.schedulejpaapi.entity.Post;
import com.example.schedulejpaapi.exceptions.custom.InvalidFieldException;
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
import java.util.Set;
import java.util.stream.Collectors;

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

    // 댓글 수정
    public CommentUpdateResponseDto updateComment(
            Long commentId,
            CommentUpdateRequestDto requestDto,
            HttpServletRequest request
    ) {
        Member loggedInMember = getLoggedInMember(request.getSession());
        Comment selectComment = getCommentById(commentId);
        verifyCommentAuthor(selectComment, loggedInMember);

        Map<String, String> requestUpdateMap = requestDto.getUpdateMap();
        verifyUpdatableField(requestUpdateMap);

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

    private Comment getCommentById(Long commentId){
        Optional<Comment> selectedComment = commentRepository.findById(commentId);
        if(selectedComment.isEmpty()) throw new NotFoundPostException("NotFound Comment");
        return selectedComment.get();
    }

    private List<Comment> getCommentListByPost(Post post){
        List<Comment> comments = commentRepository.findByPost(post);
        return comments.isEmpty() ? List.of() : comments;
    }
    private void verifyCommentAuthor(Comment comment, Member loggedInMember) {
        Long loggedInMemberId = loggedInMember.getId();
        Long commentWriterId = comment.getMember().getId();
        if (!loggedInMemberId.equals(commentWriterId)) {
            throw new UnauthorizedException("Unauthorized");
        }
    }

    // 수정 요청 필드 검증
    private void verifyUpdatableField(Map<String, String> requestMap){
        Set<String> allowedFields = Const.UPDATE_COMMENT_FIELDS.keySet();
        Set<String> requestFields = requestMap.keySet();
        Set<String> invalidFields = requestFields.stream()
                .filter(field -> !allowedFields.contains(field))
                .collect(Collectors.toSet());
        if (!invalidFields.isEmpty()) {
            throw new InvalidFieldException("Invalid Field");
        }
    }


}

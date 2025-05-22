package com.example.schedulejpaapi.service;

import com.example.schedulejpaapi.exceptions.custom.UnauthorizedException;
import com.example.schedulejpaapi.util.Validator;
import com.example.schedulejpaapi.constant.Const;
import com.example.schedulejpaapi.dto.comment.*;
import com.example.schedulejpaapi.entity.Comment;
import com.example.schedulejpaapi.entity.Member;
import com.example.schedulejpaapi.entity.Post;
import com.example.schedulejpaapi.exceptions.custom.NotFoundPostException;
import com.example.schedulejpaapi.repository.CommentRepository;
import com.example.schedulejpaapi.repository.PostRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final Validator validator;

    public CommentService(
            CommentRepository commentRepository,
            PostRepository postRepository,
            Validator validator
    ) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.validator = validator;
    }

    /**
     * 새로운 댓글 생성 및 스케줄에 등록
     *
     * @param requestDto     생성할 댓글 정보 DTO{@link CommentCreateRequestDto}
     * @param postId         댓글을 등록할 스케줄 ID
     * @param loggedInMember HTTP 요청 객체. 세션 정보 추출하여 사용
     * @return 생성된 댓글 정보 DTO{@link CommentCreateResponseDto}
     */
    @Transactional
    public CommentCreateResponseDto createComment(
            Long postId,
            CommentCreateRequestDto requestDto,
            Member loggedInMember
    ) {
        Post selectPost = getPostById(postId);
        Comment comment = new Comment(requestDto, selectPost, loggedInMember);

        Comment savedComment = commentRepository.save(comment);
        return new CommentCreateResponseDto(savedComment);
    }

    /**
     * 특정 스케줄의 댓글 목록을 조회
     *
     * @param postId 조회할 스케줄의 ID
     * @return 조회된 댓글 목록 DTO{@link CommentFindByPostResponseDto} 리스트
     */
    @Transactional(readOnly = true)
    public List<CommentFindByPostResponseDto> getCommentsByPostId(Long postId) {
        Post selectPost = getPostById(postId);
        List<Comment> comments = commentRepository.findByPost(selectPost);

        return comments.stream().map(CommentFindByPostResponseDto::new).toList();
    }

    /**
     * 특정 댓글 수정
     *
     * @param commentId      수정할 댓글 ID
     * @param requestDto     수정할 댓글 정보 DTO{@link CommentUpdateRequestDto}
     * @param loggedInMember 로그인 멤버 정보{@link Member}
     * @return 수정된 댓글 정보 DTO{@link CommentUpdateResponseDto}
     */
    //TODO QueryDsl로 변경해서 SQL 최적화 필요
    @Transactional
    public CommentUpdateResponseDto updateComment(
            Long commentId,
            CommentUpdateRequestDto requestDto,
            Member loggedInMember
    ) {
        Comment selectComment = getCommentById(commentId);
        validator.verifyAuthorOwner(
                selectComment,
                loggedInMember,
                (comment) -> comment.getPost().getId()
        );

        Map<String, String> requestUpdateMap = requestDto.getUpdateMap();
        validator.verifyUpdatableField(requestUpdateMap, Const.UPDATE_COMMENT_FIELDS.keySet());

        requestUpdateMap.forEach(
                (field, value) -> Const.UPDATE_COMMENT_FIELDS
                        .get(field)
                        .accept(selectComment, value)
        );
        Comment savedComment = commentRepository.save(selectComment);

        return new CommentUpdateResponseDto(savedComment);
    }

    /**
     * 특정 댓글 삭제
     *
     * @param commentId 삭제할 댓글 ID
     * @return 삭제된 댓글 정보 DTO{@link CommentRemoveResponseDto}
     */
    @Transactional
    public CommentRemoveResponseDto removeComment(
            Long commentId,
            Member loggedInMember
    ) {
        Comment selectComment = getCommentById(commentId);

        validator.verifyAuthorOwner(selectComment, loggedInMember, (comment) -> comment.getMember().getId());
        commentRepository.delete(selectComment);
        return new CommentRemoveResponseDto(selectComment);
    }

    /**
     * 댓글이 등록될 스케줄 조회
     *
     * @param postId 조회할 스케줄 ID
     * @return 조회된 스케줄 정보 Entity{@link Post}
     * @throws NotFoundPostException 스케줄이 존재하지 않으면 발생
     */
    private Post getPostById(Long postId) {
        Optional<Post> selectedPost = postRepository.findById(postId);
        if (selectedPost.isEmpty()) throw new NotFoundPostException("NotFound Post");
        return selectedPost.get();
    }

    /**
     * 댓글 조회
     *
     * @param commentId 조회할 댓글 ID
     * @return 조회된 댓글 Entity{@link Comment}
     */
    private Comment getCommentById(Long commentId) {
        Optional<Comment> selectedComment = commentRepository.findById(commentId);
        if (selectedComment.isEmpty()) throw new NotFoundPostException("NotFound Comment");
        return selectedComment.get();
    }
}

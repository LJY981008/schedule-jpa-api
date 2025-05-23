package com.example.schedulejpaapi.service;

import com.example.schedulejpaapi.util.Validator;
import com.example.schedulejpaapi.constant.Const;
import com.example.schedulejpaapi.dto.post.*;
import com.example.schedulejpaapi.entity.Member;
import com.example.schedulejpaapi.entity.Post;
import com.example.schedulejpaapi.exceptions.custom.NotFoundPostException;
import com.example.schedulejpaapi.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 스케줄 관련 비지니스 로직 서비스
 */
@Service
public class PostService {

    private final PostRepository postRepository;
    private final Validator validator;

    public PostService(PostRepository postRepository, Validator validator) {
        this.postRepository = postRepository;
        this.validator = validator;
    }

    /**
     * 새로운 스케줄 생성
     *
     * @param requestDto     생성할 스케줄 요청 정보 DTO{@link PostCreateRequestDto}}
     * @param loggedInMember 로그인한 멤버 정보{@link Member}
     * @return 생성된 스케줄 정보 DTO {@link PostCreateResponseDto}
     */
    @Transactional
    public PostCreateResponseDto createPost(PostCreateRequestDto requestDto, Member loggedInMember) {
        Post postSetWriter = new Post(requestDto, loggedInMember);
        Post savedPost = postRepository.save(postSetWriter);

        return new PostCreateResponseDto(savedPost);
    }

    /**
     * 특정 스케줄을 하나 조회
     *
     * @param postId 조회할 스케줄의 ID
     * @return 조회된 스케줄의 정보 DTO {@link PostFindResponseDto}
     */
    @Transactional
    public PostFindResponseDto getPostById(Long postId) {
        Optional<Post> findPost = postRepository.findById(postId);
        if (findPost.isEmpty()) throw new NotFoundPostException("NotFound Post");

        return new PostFindResponseDto(findPost.get());
    }

    /**
     * 스케줄 전체를 페이지네이션하여 조회
     * 수정일 기준 내림차순
     *
     * @param page 조회할 페이지 (0부터 시작)
     * @param size 한 페이지당 스케줄 수
     * @return 조회된 게시글 목록 {@link PostFindAllResponseDto} 리스트
     * @throws NotFoundPostException 조회 결과가 없으면 발생
     */
    @Transactional(readOnly = true)
    public List<PostFindAllResponseDto> getPosts(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "modifiedAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        List<Post> findPosts = postRepository.findAll(pageable).getContent();
        if (findPosts.isEmpty()) {
            throw new NotFoundPostException("NotFound Post");
        }

        return findPosts.stream().map(PostFindAllResponseDto::new).toList();
    }

    /**
     * 특정 스케줄의 정보를 수정
     *
     * @param postId            수정할 스케줄의 ID
     * @param requestDto        수정할 정보 DTO
     * @param loggedInMember    로그인한 멤버 정보
     */
    @Transactional
    public void updatePost(
            Long postId,
            PostUpdateRequestDto requestDto,
            Member loggedInMember
    ) {
        Post findPost = getPostAccess(postId, loggedInMember);

        Map<String, String> requestUpdateMap = requestDto.getUpdateMap();
        validator.verifyUpdatableField(requestUpdateMap, Const.UPDATE_POST_FIELDS.keySet());

        long updateResult = postRepository.updatePostOfTitleOrContents(findPost.getId(), requestUpdateMap);
        if (updateResult == 0) throw new NotFoundPostException("NotFound Post");
    }

    /**
     * 특정 스케줄 삭제
     *
     * @param postId  삭제할 스케줄 ID
     * @param loggedInMember HTTP 요청 객체. 세션 정보 추출하여 사용
     * @return 삭제된 스케줄의 정보 {@link PostRemoveResponseDto}
     */
    @Transactional
    public PostRemoveResponseDto removePost(Long postId, Member loggedInMember) {
        Post findPost = getPostAccess(postId, loggedInMember);
        findPost.getMember().getPosts().remove(findPost);

        postRepository.delete(findPost);

        return new PostRemoveResponseDto(findPost);
    }

    /**
     * 스케줄 접근권한 확인 후 스케줄 반환
     * 스케줄의 존재여부, 로그인 여부, 작성자 일치여부
     *
     * @param postId  검증할 스케줄 ID
     * @param loggedInMember 로그인한 멤버 정보{@link Member}
     * @return 접근이 허용된 스케줄 Entity {@link Post}
     */
    private Post getPostAccess(Long postId, Member loggedInMember) {
        Optional<Post> findPost = postRepository.findById(postId);
        if (findPost.isEmpty()) throw new NotFoundPostException("NotFound Post");

        validator.verifyAuthorOwner(findPost.get(), loggedInMember, (post) -> post.getMember().getId());
        return findPost.get();
    }
}

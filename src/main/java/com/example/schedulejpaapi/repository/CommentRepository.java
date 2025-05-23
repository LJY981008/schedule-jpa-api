package com.example.schedulejpaapi.repository;

import com.example.schedulejpaapi.entity.Comment;
import com.example.schedulejpaapi.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * {@link Comment} Entity에 대한 데이터 접근을 제공하는 Repository 인터페이스
 * {@link JpaRepository}를 상속받아 CRUD 수행
 */
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    /**
     * 특정 스케줄의 모든 댓글 조회
     *
     * @param post 댓글을 조회할 스케줄
     * @return 조회된 댓글 정보 {@link Comment} 리스트
     */
    List<Comment> findByPost(Post post);
}

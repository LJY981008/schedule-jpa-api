package com.example.schedulejpaapi.repository;

import com.example.schedulejpaapi.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

/**
 * {@link Post} Entity에 대한 데이터 접근을 제공하는 Repository 인터페이스
 * {@link JpaRepository}를 상속받아 CRUD 수행
 */
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    /**
     * 모든 Entity를 페이지 단위로 조회
     *
     * @param pageable 페이징 정보
     * @return 페이징된 Entity{@link Post}
     */
    @NonNull
    Page<Post> findAll(@NonNull Pageable pageable);
}

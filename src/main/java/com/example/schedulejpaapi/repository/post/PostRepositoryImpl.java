package com.example.schedulejpaapi.repository.post;

import com.example.schedulejpaapi.entity.QPost;
import com.example.schedulejpaapi.exception.custom.IllegalSqlStatementException;
import com.example.schedulejpaapi.enums.PostUpdateField;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * 댓글 수정
     *
     * @param postId 스케줄 ID
     * @param requestMap 수정 요청 MAP
     * @return 수정된 데이터 개수
     */
    @Override
    public long updatePost(Long postId, Map<String, String> requestMap) {
        if(requestMap.isEmpty()) {
            throw new IllegalSqlStatementException("empty request content");
        }

        QPost post = QPost.post;
        JPAUpdateClause updateClause = queryFactory.update(post).where(post.id.eq(postId));

        for(Map.Entry<String, String> entry : requestMap.entrySet()) {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue();

            Optional<PostUpdateField> updateField = Optional.ofNullable(PostUpdateField.fromFieldName(fieldName));
            if(updateField.isEmpty()) throw new IllegalSqlStatementException("illegal field name");

            Path<?> qPath = updateField.get().getQPath();
            updateClause.set((StringPath) qPath, fieldValue);
        }

        return updateClause.execute();
    }
}

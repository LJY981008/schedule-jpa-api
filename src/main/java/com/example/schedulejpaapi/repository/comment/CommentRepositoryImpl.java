package com.example.schedulejpaapi.repository.comment;

import com.example.schedulejpaapi.entity.QComment;
import com.example.schedulejpaapi.enums.CommentUpdateField;
import com.example.schedulejpaapi.exception.custom.IllegalSqlStatementException;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public long updateComment(long commentId, Map<String, String> requestMap) {
        if(requestMap.isEmpty()) {
            throw new IllegalSqlStatementException("empty request content");
        }

        QComment comment = QComment.comment;
        JPAUpdateClause updateClause = queryFactory.update(comment).where(comment.id.eq(commentId));

        for(Map.Entry<String, String> entry : requestMap.entrySet()) {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue();

            Optional<CommentUpdateField> updateField = Optional.ofNullable(CommentUpdateField.fromFieldName(fieldName));
            if(updateField.isEmpty()) throw new IllegalSqlStatementException("illegal field name");

            Path<?> qPath = updateField.get().getQPath();
            updateClause.set((StringPath) qPath, fieldValue);
        }

        return updateClause.execute();
    }
}

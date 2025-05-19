package com.example.schedulejpaapi.repository;

import com.example.schedulejpaapi.entity.Comment;
import com.example.schedulejpaapi.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}

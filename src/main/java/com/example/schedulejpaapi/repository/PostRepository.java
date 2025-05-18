package com.example.schedulejpaapi.repository;

import com.example.schedulejpaapi.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}

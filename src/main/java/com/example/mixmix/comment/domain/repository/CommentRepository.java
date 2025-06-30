package com.example.mixmix.comment.domain.repository;

import com.example.mixmix.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByFeedId(Long feedId, Pageable pageable);
}
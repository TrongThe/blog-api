package com.example.blogapi.repository;

import com.example.blogapi.entity.Post;
import com.example.blogapi.entity.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PostRepository
        extends JpaRepository<Post, Long>,
                JpaSpecificationExecutor<Post> {

    Page<Post> findByStatus(PostStatus status, Pageable pageable);

}

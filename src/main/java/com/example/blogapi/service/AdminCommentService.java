package com.example.blogapi.service;


import com.example.blogapi.dto.response.CommentResponse;
import com.example.blogapi.entity.Comment;
import com.example.blogapi.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminCommentService {

    private final CommentRepository commentRepository;

    public Page<CommentResponse> getComments(
            Pageable pageable
    ){
        return commentRepository
                .findAll(pageable)
                .map(this :: toResponse);
    }

    private CommentResponse toResponse(Comment comment){
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getUsername(),
                comment.getPost().getId(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}

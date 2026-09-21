package com.example.blogapi.service;


import com.example.blogapi.dto.response.CommentResponse;
import com.example.blogapi.mapper.CommentMapper;
import com.example.blogapi.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.example.blogapi.constant.AppConstants.PAGE_SIZE;

@Service
@RequiredArgsConstructor
public class AdminCommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public Page<CommentResponse> getComments(
            int page
    ){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        return commentRepository
                .findAll(pageable)
                .map(commentMapper :: toResponse);
    }

}

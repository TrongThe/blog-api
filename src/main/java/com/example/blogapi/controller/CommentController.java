package com.example.blogapi.controller;


import com.example.blogapi.dto.request.CommentCreateRequest;
import com.example.blogapi.dto.request.CommentUpdateRequest;
import com.example.blogapi.dto.response.CommentResponse;
import com.example.blogapi.service.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "bearerAuth")
    public CommentResponse create(
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequest request,
            Authentication authentication
    ){
        return commentService.create(
                postId,
                request,
                authentication
        );
    }

    @GetMapping
    public Page<CommentResponse> getComments(
            @PathVariable Long postId,
            Pageable pageable
    ){
        return commentService.getComments(postId,pageable);
    }

}

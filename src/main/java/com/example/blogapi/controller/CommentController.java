package com.example.blogapi.controller;


import com.example.blogapi.dto.request.CommentCreateRequest;
import com.example.blogapi.dto.request.CommentUpdateRequest;
import com.example.blogapi.dto.response.BaseResponse;
import com.example.blogapi.dto.response.CommentResponse;
import com.example.blogapi.enums.MessageKey;
import com.example.blogapi.service.CommentService;
import com.example.blogapi.util.MessageUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final MessageUtil messageUtil;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "bearerAuth")
    public BaseResponse<CommentResponse> create(
            @PathVariable Long postId,
            @Valid @RequestBody CommentCreateRequest request,
            Authentication authentication,
            Locale locale
    ){
        return BaseResponse.success(
                messageUtil.getMessage(MessageKey.COMMENT_CREATED, locale),
                commentService.create(
                postId,
                request,
                authentication
        ));
    }

    @GetMapping
    public BaseResponse<Page<CommentResponse>> getComments(
            @PathVariable Long postId,
            Pageable pageable
    ){
        return BaseResponse.success(commentService.getComments(postId,pageable));
    }

}

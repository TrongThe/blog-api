package com.example.blogapi.controller;


import com.example.blogapi.dto.request.CommentUpdateRequest;
import com.example.blogapi.dto.response.BaseResponse;
import com.example.blogapi.dto.response.CommentResponse;
import com.example.blogapi.enums.MessageKey;
import com.example.blogapi.service.CommentService;
import com.example.blogapi.util.MessageUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CommentManagementController {

    private final CommentService commentService;
    private final MessageUtil messageUtil;

    @PutMapping("/{commentId}")
    public BaseResponse<CommentResponse> update(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequest request,
            Authentication authentication,
            Locale locale
    ){
        return BaseResponse.success(
                messageUtil.getMessage(MessageKey.COMMENT_UPDATED, locale),
                commentService.update(commentId, request, authentication));
    }

    @DeleteMapping("/{commentId}")
    public BaseResponse<Void> delete(
            @PathVariable Long commentId,
            Authentication authentication,
            Locale locale
    ){
        commentService.delete(commentId,authentication);

        return BaseResponse.success(
                messageUtil.getMessage(MessageKey.COMMENT_DELETED, locale),
                null
        );
    }
}

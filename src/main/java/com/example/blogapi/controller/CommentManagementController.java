package com.example.blogapi.controller;


import com.example.blogapi.dto.request.CommentUpdateRequest;
import com.example.blogapi.dto.response.BaseResponse;
import com.example.blogapi.dto.response.CommentResponse;
import com.example.blogapi.service.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CommentManagementController {

    private final CommentService commentService;

    @PutMapping("/{commentId}")
    public BaseResponse<CommentResponse> update(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequest request,
            Authentication authentication
    ){
        return BaseResponse.success(
                "comment.updated",
                commentService.update(commentId, request, authentication));
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BaseResponse<Void> delete(
            @PathVariable Long commentId,
            Authentication authentication
    ){
        commentService.delete(commentId,authentication);

        return BaseResponse.success(
                "comment.deleted",
                null
        );
    }
}

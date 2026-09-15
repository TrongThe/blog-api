package com.example.blogapi.controller;


import com.example.blogapi.dto.request.PostSearchRequest;
import com.example.blogapi.dto.request.PostUpdateRequest;
import com.example.blogapi.dto.response.BaseResponse;
import com.example.blogapi.dto.response.PostResponse;
import com.example.blogapi.dto.request.PostCreateRequest;
import com.example.blogapi.enums.MessageKey;
import com.example.blogapi.service.PostService;
import com.example.blogapi.util.MessageUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
     private final MessageUtil messageUtil;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "bearerAuth")
    public BaseResponse<PostResponse> createPost(
            @Valid @RequestBody PostCreateRequest request,
            Authentication authentication,
            Locale locale
    ){

        return BaseResponse.success(
                messageUtil.getMessage(MessageKey.POST_CREATED, locale),
                postService.create(request, authentication.getName()));
    }

    @GetMapping
    public BaseResponse<Page<PostResponse>>getPublishedPosts(Pageable pageable){

        return BaseResponse.success(postService.getPublishedPosts(pageable));
    }

    @GetMapping("/search")
    public BaseResponse<Page<PostResponse>> search(
            @ModelAttribute PostSearchRequest request,
            Pageable pageable,
            Authentication authentication){

        return BaseResponse.success(postService.search(request,pageable,authentication));
    }

    @GetMapping("/{id}")
    public BaseResponse<PostResponse> getPost(
            @PathVariable Long id,
            Authentication authentication
    ){
        return BaseResponse.success(postService.getPost(id, authentication));
    }

    @PatchMapping("/{id}/publish")
    @SecurityRequirement(name = "bearerAuth")
    public BaseResponse<PostResponse> publish(
            @PathVariable Long id,
            Authentication authentication,
            Locale locale
    ){
        return BaseResponse.success(
                messageUtil.getMessage(MessageKey.POST_PUBLISHED, locale),
                postService.publish(id, authentication));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public BaseResponse<PostResponse> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostUpdateRequest request,
            Authentication authentication,
            Locale locale
    ){
        return BaseResponse.success(
                messageUtil.getMessage(MessageKey.POST_UPDATED, locale),
                postService.update(id, request, authentication));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public BaseResponse<Void> deletePost(
            @PathVariable Long id,
            Authentication authentication,
            Locale locale
    ){
        postService.delete(id, authentication);

        return BaseResponse.success(
                messageUtil.getMessage(MessageKey.POST_DELETED, locale),
                null
        );
    }
}

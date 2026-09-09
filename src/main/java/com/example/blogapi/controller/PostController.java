package com.example.blogapi.controller;


import com.example.blogapi.dto.request.PostSearchRequest;
import com.example.blogapi.dto.request.PostUpdateRequest;
import com.example.blogapi.dto.response.BaseResponse;
import com.example.blogapi.dto.response.PostResponse;
import com.example.blogapi.dto.request.PostCreateRequest;
import com.example.blogapi.service.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "bearerAuth")
    public BaseResponse<PostResponse> createPost(
            @Valid @RequestBody PostCreateRequest request,
            Authentication authentication
            ){

        return BaseResponse.success(
                "post.created",
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
            Authentication authentication
    ){
        return BaseResponse.success(
                "post.published",
                postService.publish(id, authentication));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public BaseResponse<PostResponse> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostUpdateRequest request,
            Authentication authentication
            ){
        return BaseResponse.success(
                "post.updated",
                postService.update(id, request, authentication));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "bearerAuth")
    public BaseResponse<Void> deletePost(
            @PathVariable Long id,
            Authentication authentication
    ){
        postService.delete(id, authentication);

        return BaseResponse.success(
                "post.deleted",
                null
        );
    }
}

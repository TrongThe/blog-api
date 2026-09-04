package com.example.blogapi.controller;


import com.example.blogapi.dto.response.CommentResponse;
import com.example.blogapi.service.AdminCommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class AdminCommentController {

    private final AdminCommentService adminCommentService;

    @GetMapping
    public Page<CommentResponse> getComments(Pageable pageable){
        return adminCommentService.getComments(pageable);
    }
}

package com.example.blogapi.controller;


import com.example.blogapi.dto.request.CategoryCreateRequest;
import com.example.blogapi.dto.request.CategoryUpdateRequest;
import com.example.blogapi.dto.response.BaseResponse;
import com.example.blogapi.dto.response.CategoryResponse;
import com.example.blogapi.service.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<CategoryResponse> create(
            @Valid @RequestBody CategoryCreateRequest request
            ){
        return BaseResponse.success(
                "Category created successfully",
                categoryService.create(request)
        );
    }

    @GetMapping
    public BaseResponse<List<CategoryResponse>> getAll(){

        return BaseResponse.success(categoryService.getAll());
    }

    @GetMapping("/{id}")
    public BaseResponse<CategoryResponse> getById(
            @PathVariable Long id
    ) {
        return BaseResponse.success(categoryService.getById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public BaseResponse<CategoryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateRequest request
    ) {
        return BaseResponse.success(
                "Category updated successfully",
                categoryService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BaseResponse<Void> delete(
            @PathVariable Long id
    ) {
        categoryService.delete(id);

        return BaseResponse.success(
                "Category deleted successfully",
                null
        );
    }

}

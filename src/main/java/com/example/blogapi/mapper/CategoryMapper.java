package com.example.blogapi.mapper;


import com.example.blogapi.dto.response.CategoryResponse;
import com.example.blogapi.entity.Category;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);

    Set<CategoryResponse> toResponseSet(Set<Category> categories);
}

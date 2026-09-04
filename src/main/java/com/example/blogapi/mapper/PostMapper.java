package com.example.blogapi.mapper;


import com.example.blogapi.dto.response.CategoryResponse;
import com.example.blogapi.dto.response.PostResponse;
import com.example.blogapi.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public PostResponse toResponse(Post post){
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getStatus(),
                post.getAuthor().getId(),
                post.getAuthor().getUsername(),
                post.getCategories()
                        .stream()
                        .map(category -> new CategoryResponse(
                                category.getId(),
                                category.getName(),
                                category.getDescription()
                        ))
                        .collect(java.util.stream.Collectors.toSet()),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}

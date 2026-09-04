package com.example.blogapi.mapper;


import com.example.blogapi.dto.response.CommentResponse;
import com.example.blogapi.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "username", source = "author.username")
    @Mapping(target = "postId", source = "post.id")
    CommentResponse toResponse(Comment comment);

    List<CommentResponse> toResponseList(List<Comment> comments);

    Set<CommentResponse> toResponseSet(Set<Comment> comments);
}

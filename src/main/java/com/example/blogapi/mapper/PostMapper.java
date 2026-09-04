package com.example.blogapi.mapper;



import com.example.blogapi.dto.response.PostResponse;
import com.example.blogapi.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface PostMapper {

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorUsername", source = "author.username")
    PostResponse toResponse(Post post);

    Set<PostResponse> toResponseSet(Set<Post> posts);

    List<PostResponse> toResponseList(List<Post> posts);

}

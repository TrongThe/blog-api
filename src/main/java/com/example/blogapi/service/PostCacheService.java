package com.example.blogapi.service;


import com.example.blogapi.dto.response.PostResponse;
import com.example.blogapi.entity.Post;
import com.example.blogapi.entity.PostStatus;
import com.example.blogapi.exception.ResourceNotFoundException;
import com.example.blogapi.mapper.PostMapper;
import com.example.blogapi.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCacheService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Cacheable(value = "posts", key = "#id")
    @Transactional
    public PostResponse getPublishedPostId(Long id){

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("post.notfound"));

        if (post.getStatus() != PostStatus.PUBLISHED){
            throw new ResourceNotFoundException("post.notfound");
        }

        return postMapper.toResponse(post);
    }
}

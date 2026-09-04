package com.example.blogapi.service;


import com.example.blogapi.dto.request.PostSearchRequest;
import com.example.blogapi.dto.request.PostUpdateRequest;
import com.example.blogapi.dto.response.PostResponse;
import com.example.blogapi.dto.request.PostCreateRequest;
import com.example.blogapi.entity.*;
import com.example.blogapi.event.PostCreatedEvent;
import com.example.blogapi.event.PostEventProducer;
import com.example.blogapi.exception.ForbiddenException;
import com.example.blogapi.exception.ResourceNotFoundException;
import com.example.blogapi.mapper.PostMapper;
import com.example.blogapi.repository.CategoryRepository;
import com.example.blogapi.repository.OutboxEventRepository;
import com.example.blogapi.repository.PostRepository;
import com.example.blogapi.repository.UserRepository;
import com.example.blogapi.repository.specification.PostSpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PostMapper postMapper;
    private final PostAuthorizationService postAuthorizationService;
    private final PostEventProducer postEventProducer;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final PostCacheService postCacheService;

    @Transactional
    public PostResponse create(PostCreateRequest request, String username) {

        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Category> categoryList = categoryRepository.findAllById(request.categoryIds());

        if (categoryList.size() != request.categoryIds().size()){
            throw new ResourceNotFoundException("One or more categories not found");
        }

        Set<Category> categories = new HashSet<>(categoryList);

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .author(author)
                .categories(categories)
                .build();

        Post savedPost = postRepository.save(post);

        PostCreatedEvent event = new PostCreatedEvent(
                savedPost.getId(),
                savedPost.getTitle(),
                savedPost.getAuthor().getId(),
                savedPost.getAuthor().getUsername()
        );

        try {
            String payload = objectMapper.writeValueAsString(event);

            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .eventType("POST_CREATED")
                    .payload(payload)
                    .published(false)
                    .build();

            outboxEventRepository.save(outboxEvent);
        } catch (JsonProcessingException e){
            throw new RuntimeException("Failed to serialize event", e);
        }

        return postMapper.toResponse(savedPost);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getPublishedPosts(Pageable pageable){
        return postRepository
                .findByStatus(PostStatus.PUBLISHED, pageable)
                .map(postMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id, Authentication authentication){

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (post.getStatus() == PostStatus.PUBLISHED){
            return postCacheService.getPublishedPostId(id);
        }

        if (authentication == null || !authentication.isAuthenticated()){
            throw new ResourceNotFoundException("Post not found");
        }

        if (!postAuthorizationService.canManage(post,authentication)){
            throw new ResourceNotFoundException("Post not found");
        }

        return postMapper.toResponse(post);
    }

    @CacheEvict(value = "posts", key = "#postId")
    @Transactional
    public PostResponse publish(Long postId, Authentication authentication){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!postAuthorizationService.canManage(post,authentication)){
            throw new ForbiddenException("You are not allowed to publish this post");
        }

        if (post.getStatus() == PostStatus.PUBLISHED){
            throw new RuntimeException("Post is already publish");
        }

        post.setStatus(PostStatus.PUBLISHED);

        return postMapper.toResponse(post);
    }

    @CacheEvict(value = "posts", key = "#postId")
    @Transactional
    public PostResponse update(
            Long postId,
            PostUpdateRequest request,
            Authentication authentication
    ){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!postAuthorizationService.canManage(post,authentication)){
            throw new ForbiddenException("You are not allowed to update this post");
        }

        List<Category> categoryList = categoryRepository.findAllById(request.categoryIds());

        if (categoryList.size() != request.categoryIds().size()){
            throw new ResourceNotFoundException("One or more categories not found");
        }

        post.setTitle(request.title());
        post.setContent(request.content());
        post.setCategories(new HashSet<>(categoryList));

        return postMapper.toResponse(post);
    }

    @CacheEvict(value = "posts", key = "#postId")
    @Transactional
    public void delete(Long postId,
                       Authentication authentication){

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!postAuthorizationService.canManage(post,authentication)){
            throw new ForbiddenException("You are not allowed to delete this post");
        }

        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> search(
            PostSearchRequest request,
            Pageable pageable,
            Authentication authentication
    ){
        Specification<Post> specification = Specification.where(
                PostSpecification.hasTitle(request.title())
        )
                .and(
                        PostSpecification.hasAuthor(request.author())
                )
                .and(
                        PostSpecification.hasCategory(request.categoryId())
                );

        if (authentication == null || !authentication.isAuthenticated()){

            specification = specification.and(
                    PostSpecification.hasStatus(PostStatus.PUBLISHED)
            );

        } else {

            String username = authentication.getName();

            if (postAuthorizationService.isAdmin(authentication)){

                if (request.status() != null){
                    specification = specification.and(
                            PostSpecification.hasStatus(request.status())
                    );
                }
            } else {

                specification = specification.and(
                        PostSpecification.visibleToUser(username)
                );

                if (request.status() != null){

                    specification = specification.and(
                            PostSpecification.hasStatus(request.status())
                    );
                }
            }
        }


        return postRepository
                .findAll(specification, pageable)
                .map(postMapper :: toResponse);
    }

}

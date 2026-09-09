package com.example.blogapi.service;


import com.example.blogapi.dto.request.CommentCreateRequest;
import com.example.blogapi.dto.request.CommentUpdateRequest;
import com.example.blogapi.dto.response.CommentResponse;
import com.example.blogapi.entity.Comment;
import com.example.blogapi.entity.Post;
import com.example.blogapi.entity.PostStatus;
import com.example.blogapi.entity.User;
import com.example.blogapi.exception.ForbiddenException;
import com.example.blogapi.exception.ResourceNotFoundException;
import com.example.blogapi.mapper.CommentMapper;
import com.example.blogapi.repository.CommentRepository;
import com.example.blogapi.repository.PostRepository;
import com.example.blogapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentAuthorizationService commentAuthorizationService;
    private final CommentMapper commentMapper;

    @Transactional
    public CommentResponse create(
            Long postId,
            CommentCreateRequest request,
            Authentication authentication
    ){
        String username = authentication.getName();

        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("user.notfound"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post.notfound"));

        if (post.getStatus() != PostStatus.PUBLISHED){
            throw new ResourceNotFoundException("post.notfound");
        }

        Comment comment = Comment.builder()
                .content(request.content())
                .author(author)
                .post(post)
                .build();

        Comment savedComment = commentRepository.save(comment);

        return commentMapper.toResponse(savedComment);

    }

    @Transactional(readOnly = true)
    public Page<CommentResponse> getComments(
            Long postId,
            Pageable pageable
    ){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post.notfound"));

        if (post.getStatus() != PostStatus.PUBLISHED){
            throw new ResourceNotFoundException("post.notfound");
        }

        return commentRepository
                .findByPostId(postId, pageable)
                .map(commentMapper::toResponse);
    }

    @Transactional
    public CommentResponse update(
            Long commentId,
            CommentUpdateRequest request,
            Authentication authentication
    ){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("comment.notfound"));

        if (!commentAuthorizationService.canManage(comment, authentication)){
            throw new ForbiddenException("access.denied");
        }

        comment.setContent(request.content());

        return commentMapper.toResponse(comment);
    }

    @Transactional
    public void delete(
            Long commentId,
            Authentication authentication
    ){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("comment.notfound"));

        if (!commentAuthorizationService.canManage(comment, authentication)){
            throw new ForbiddenException("access.denied");
        }

        commentRepository.delete(comment);
    }
}

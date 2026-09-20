package com.example.blogapi.service;


import com.example.blogapi.dto.request.PostSearchRequest;
import com.example.blogapi.entity.Post;
import com.example.blogapi.entity.PostStatus;
import com.example.blogapi.repository.specification.PostSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostVisibilityService {

    private final PostAuthorizationService postAuthorizationService;

    public Specification<Post> buildVisibilitySpecification(
            PostSearchRequest request,
            Authentication authentication
    ){
        if (authentication == null
                || authentication instanceof AnonymousAuthenticationToken) {

            return PostSpecification.hasStatus(PostStatus.PUBLISHED);
        }

        String username = authentication.getName();

        if (postAuthorizationService.isAdmin(authentication)) {

            if (request.status() != null) {
                return PostSpecification.hasStatus(request.status());
            }

            return null;
        }

        Specification<Post> specification =
                PostSpecification.visibleToUser(username);

        if (request.status() != null) {
            specification = specification.and(
                    PostSpecification.hasStatus(request.status())
            );
        }

        return specification;
    }
}

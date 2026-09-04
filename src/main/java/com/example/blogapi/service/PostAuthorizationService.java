package com.example.blogapi.service;


import com.example.blogapi.entity.Post;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class PostAuthorizationService {

    public boolean isAdmin(Authentication authentication){
        return authentication != null
                && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean isOwner(Post post, Authentication authentication){
        return authentication != null
                && authentication.isAuthenticated()
                && post.getAuthor().getUsername().equals(authentication.getName());
    }

    public boolean canManage(Post post, Authentication authentication){
        return isAdmin(authentication)
                || isOwner(post,authentication);
    }
}

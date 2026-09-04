package com.example.blogapi.service;

import com.example.blogapi.entity.Comment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
public class CommentAuthorizationService {

    public boolean isAdmin(Authentication authentication){
        return authentication != null
                && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean isOwner(
            Comment comment,
            Authentication authentication
    ){
        return authentication != null
                && authentication.isAuthenticated()
                && comment.getAuthor().getUsername().equals(authentication.getName());
    }

    public boolean canManage(
            Comment comment,
            Authentication authentication
    ){
        return isAdmin(authentication) || isOwner(comment, authentication);
    }
}

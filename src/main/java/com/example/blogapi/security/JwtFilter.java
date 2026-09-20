package com.example.blogapi.security;


import com.example.blogapi.service.JwtService;
import com.example.blogapi.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        System.out.println("=== JWT FILTER ===");
        System.out.println("URI: " + request.getRequestURI());
        System.out.println("Authorization: " + authHeader);
        System.out.println("Auth before: " +
                SecurityContextHolder.getContext().getAuthentication());

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            String username = jwtService.extractUsername(token);

            if (username != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

                Long userId = userDetails.getId();

                System.out.println("Username: " + username);
                System.out.println("User ID: " + userId);
                System.out.println("Enabled: " + userDetails.isEnabled());
                System.out.println("Access token valid: "
                        + tokenService.isValidAccessToken(userId, token));

                if (userDetails.isEnabled() && tokenService.isValidAccessToken(userId, token)) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);

                    System.out.println("=== AUTHENTICATION SET ===");
                    System.out.println(
                            SecurityContextHolder.getContext().getAuthentication()
                    );
                }
            }

        } catch (Exception e) {
            System.out.println("==== JWT ERROR ====");
            e.printStackTrace();
        }

        System.out.println("=== JWT FILTER ===");
        System.out.println("URI: " + request.getRequestURI());
        System.out.println("Auth: " +
                SecurityContextHolder.getContext().getAuthentication());
        filterChain.doFilter(request, response);
    }

}

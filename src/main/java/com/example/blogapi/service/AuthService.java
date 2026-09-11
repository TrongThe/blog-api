package com.example.blogapi.service;


import com.example.blogapi.dto.request.LoginRequest;
import com.example.blogapi.dto.request.RefreshTokenRequest;
import com.example.blogapi.dto.request.RegisterRequest;
import com.example.blogapi.dto.response.LoginResponse;
import com.example.blogapi.entity.Role;
import com.example.blogapi.entity.User;
import com.example.blogapi.exception.ConflictException;
import com.example.blogapi.exception.InvalidCredentialsException;
import com.example.blogapi.exception.ResourceNotFoundException;
import com.example.blogapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SessionService sessionService;

    @Transactional
    public void register(RegisterRequest request){

        if (userRepository.existsByUsername(request.username())){
            throw new ConflictException("Username already exists");
        }

        if (userRepository.existsByEmail(request.email())){
            throw new ConflictException("Email already exists");
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .enabled(true)
                .build();

        userRepository.save(user);
    }

    @Transactional
    public LoginResponse login(LoginRequest request){

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!user.isEnabled()){
            throw new RuntimeException("User account is disabled");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String sessionId = UUID.randomUUID().toString();
        String accessToken = jwtService.generateToken(user, sessionId);
        String refreshToken = jwtService.generateRefreshToken(user);

        sessionService.createSession(user.getId(), sessionId, refreshToken);

        return new LoginResponse(
                accessToken,
                refreshToken
        );
    }

    @Transactional
    public LoginResponse refreshToken(RefreshTokenRequest request) {

        String username = jwtService.extractUsername(request.refreshToken());

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid refresh token"));

        String storedToken = sessionService.getRefreshToken(user.getId());

        if (storedToken == null ||
                !storedToken.equals(request.refreshToken())) {
            throw new InvalidCredentialsException(
                    "Invalid refresh token"
            );
        }

        if (!user.isEnabled()){
            throw new RuntimeException("User account is disable");
        }

        String sessionId = sessionService.getSessionId(user.getId());
        String newAccessToken = jwtService.generateToken(user, sessionId);

        return new LoginResponse(
                newAccessToken,
                request.refreshToken()
        );
    }

    @Transactional
    public void logout(Authentication authentication){

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        sessionService.deleteSessionId(user.getId());
    }
}

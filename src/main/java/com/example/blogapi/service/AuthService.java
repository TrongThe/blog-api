package com.example.blogapi.service;


import com.example.blogapi.dto.request.LoginRequest;
import com.example.blogapi.dto.request.RefreshTokenRequest;
import com.example.blogapi.dto.request.RegisterRequest;
import com.example.blogapi.dto.response.LoginResponse;
import com.example.blogapi.entity.Role;
import com.example.blogapi.entity.User;
import com.example.blogapi.enums.MessageKey;
import com.example.blogapi.exception.ConflictException;
import com.example.blogapi.exception.ForbiddenException;
import com.example.blogapi.exception.InvalidCredentialsException;
import com.example.blogapi.exception.ResourceNotFoundException;
import com.example.blogapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void register(RegisterRequest request){

        if (userRepository.existsByUsername(request.username())){
            throw new ConflictException(MessageKey.AUTH_USERNAME_EXISTS);
        }

        if (userRepository.existsByEmail(request.email())){
            throw new ConflictException(MessageKey.AUTH_EMAIL_EXISTS);
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
                .orElseThrow(() -> new InvalidCredentialsException(MessageKey.AUTH_INVALID));

        if (!user.isEnabled()){
            throw new ForbiddenException(MessageKey.AUTH_ACCOUNT_DISABLED);
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new InvalidCredentialsException(MessageKey.AUTH_INVALID);
        }

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        refreshTokenService.save(
                user.getId(),
                refreshToken,
                jwtService.getRefreshExpiration()
        );

        return new LoginResponse(
                accessToken,
                refreshToken
        );
    }

    @Transactional
    public LoginResponse refreshToken(RefreshTokenRequest request) {

        String username = jwtService.extractUsername(request.refreshToken());

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException(MessageKey.AUTH_REFRESH_TOKEN_INVALID));

        String storedToken = refreshTokenService.get(user.getId());

        if (storedToken == null ||
                !storedToken.equals(request.refreshToken())) {
            throw new InvalidCredentialsException(MessageKey.AUTH_REFRESH_TOKEN_INVALID);
        }

        if (!user.isEnabled()){
            throw new ForbiddenException(MessageKey.AUTH_ACCOUNT_DISABLED);
        }

        String newAccessToken = jwtService.generateToken(user);

        return new LoginResponse(
                newAccessToken,
                request.refreshToken()
        );
    }

    @Transactional
    public void logout(Authentication authentication){

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException(MessageKey.USER_NOT_FOUND));

        refreshTokenService.delete(user.getId());
    }
}

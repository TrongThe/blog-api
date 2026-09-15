package com.example.blogapi.controller;


import com.example.blogapi.dto.request.LoginRequest;
import com.example.blogapi.dto.request.RefreshTokenRequest;
import com.example.blogapi.dto.request.RegisterRequest;
import com.example.blogapi.dto.response.BaseResponse;
import com.example.blogapi.dto.response.LoginResponse;
import com.example.blogapi.enums.MessageKey;
import com.example.blogapi.service.AuthService;
import com.example.blogapi.util.MessageUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final MessageUtil messageUtil;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<Void> register(
            @Valid @RequestBody RegisterRequest request,
            Locale locale
    ) {
        authService.register(request);

        return BaseResponse.success(
                messageUtil.getMessage(MessageKey.USER_CREATED, locale),
                null
        );
    }

    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return BaseResponse.success(authService.login(request));
    }

    @PostMapping("/refresh")
    public BaseResponse<LoginResponse> refreshToken(
            @RequestBody RefreshTokenRequest request,
            Locale locale
    ){
        return BaseResponse.success(
                messageUtil.getMessage(MessageKey.AUTH_REFRESH, locale),
                authService.refreshToken(request));
    }

    @PostMapping("/logout")
    public BaseResponse<Void> logout(Authentication authentication, Locale locale){

        authService.logout(authentication);

        return BaseResponse.success(
                messageUtil.getMessage(MessageKey.AUTH_LOGOUT, locale),
                null
        );
    }
}

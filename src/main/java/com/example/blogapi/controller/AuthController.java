package com.example.blogapi.controller;


import com.example.blogapi.auth.LoginResult;
import com.example.blogapi.dto.request.LoginRequest;
import com.example.blogapi.dto.request.RefreshTokenRequest;
import com.example.blogapi.dto.request.RegisterRequest;
import com.example.blogapi.dto.response.BaseResponse;
import com.example.blogapi.dto.response.LoginResponse;
import com.example.blogapi.enums.MessageKey;
import com.example.blogapi.service.AuthService;
import com.example.blogapi.util.CookieUtil;
import com.example.blogapi.util.MessageUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final CookieUtil cookieUtil;

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
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        LoginResult result = authService.login(request);

        cookieUtil.addRefreshTokenCookie(response, result.refreshToken());

        return BaseResponse.success(new LoginResponse(result.accessToken()));
    }

    @PostMapping("/refresh")
    public BaseResponse<LoginResponse> refreshToken(
            HttpServletRequest request,
            Locale locale
    ){
        String refreshToken = cookieUtil.getRefreshToken(request);

        return BaseResponse.success(
                messageUtil.getMessage(MessageKey.AUTH_REFRESH, locale),
                authService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public BaseResponse<Void> logout(
            Authentication authentication,
            HttpServletResponse response,
            Locale locale){

        authService.logout(authentication);

        cookieUtil.deleteRefreshTokenCookie(response);

        return BaseResponse.success(
                messageUtil.getMessage(MessageKey.AUTH_LOGOUT, locale),
                null
        );
    }
}

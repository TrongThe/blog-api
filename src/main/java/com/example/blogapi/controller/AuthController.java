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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @SecurityRequirement(name = "bearerAuth")
    public BaseResponse<LoginResponse> refreshToken(
            @RequestHeader("Authorization") String authorizationHeader,
            HttpServletRequest request,
            Locale locale
    ){
        String accessToken = authorizationHeader.substring(7);

        String refreshToken = cookieUtil.getRefreshToken(request);

        return BaseResponse.success(
                messageUtil.getMessage(MessageKey.AUTH_REFRESH, locale),
                authService.refreshToken(accessToken,refreshToken));
    }

    @PostMapping("/logout")
    @SecurityRequirement(name = "bearerAuth")
    public BaseResponse<Void> logout(
            @RequestHeader("Authorization") String authorizationHeader,
            Authentication authentication,
            HttpServletResponse response,
            Locale locale){

        String accessToken = authorizationHeader.substring(7);

        authService.logout(accessToken,authentication);

        cookieUtil.deleteRefreshTokenCookie(response);

        return BaseResponse.success(
                messageUtil.getMessage(MessageKey.AUTH_LOGOUT, locale),
                null
        );
    }
}

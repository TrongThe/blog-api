package com.example.blogapi.controller;


import com.example.blogapi.dto.request.LoginRequest;
import com.example.blogapi.dto.request.RefreshTokenRequest;
import com.example.blogapi.dto.request.RegisterRequest;
import com.example.blogapi.dto.response.BaseResponse;
import com.example.blogapi.dto.response.LoginResponse;
import com.example.blogapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<Void> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        authService.register(request);

        return BaseResponse.success(
                "Register successfully",
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
            @RequestBody RefreshTokenRequest request
    ){
        return BaseResponse.success(
                "Refresh successfully",
                authService.refreshToken(request));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BaseResponse<Void> logout(Authentication authentication){

        authService.logout(authentication);

        return BaseResponse.success(
                "Logout successfully",
                null
        );
    }
}

package com.example.blogapi.controller;


import com.example.blogapi.dto.request.ChangeRoleRequest;
import com.example.blogapi.dto.request.ChangeUserStatusRequest;
import com.example.blogapi.dto.response.UserResponse;
import com.example.blogapi.service.AdminUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public Page<UserResponse> getUsers(Pageable pageable){
        return adminUserService.getUsers(pageable);
    }

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable Long userId){
        return adminUserService.getUser(userId);
    }

    @PatchMapping("/{userId}/role")
    public UserResponse changeRole(
            @PathVariable Long userId,
            @Valid @RequestBody ChangeRoleRequest request
    ){
        return adminUserService.changeRole(userId, request);
    }

    @PatchMapping("/{userId}/status")
    public UserResponse changeStatus(
            @PathVariable Long userId,
            @Valid @RequestBody ChangeUserStatusRequest request
    ){
        return adminUserService.changeStatus(userId,request);
    }
}

package com.example.blogapi.service;


import com.example.blogapi.dto.request.ChangeRoleRequest;
import com.example.blogapi.dto.request.ChangeUserStatusRequest;
import com.example.blogapi.dto.response.UserResponse;
import com.example.blogapi.entity.User;
import com.example.blogapi.exception.ResourceNotFoundException;
import com.example.blogapi.mapper.UserMapper;
import com.example.blogapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public Page<UserResponse> getUsers(Pageable pageable){
        return userRepository
                .findAll(pageable)
                .map(userMapper :: toResponse);
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse changeRole(
            Long userId,
            ChangeRoleRequest request
    ){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setRole(request.role());

        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse changeStatus(
            Long userId,
            ChangeUserStatusRequest request
    ){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setEnabled(request.enabled());

        if (!request.enabled()){
            user.setRefreshToken(null);
        }

        return userMapper.toResponse(user);
    }
}

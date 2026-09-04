package com.example.blogapi.mapper;


import com.example.blogapi.dto.response.UserResponse;
import com.example.blogapi.entity.User;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);

    Set<UserResponse> toResponseSet(Set<User> users);
}

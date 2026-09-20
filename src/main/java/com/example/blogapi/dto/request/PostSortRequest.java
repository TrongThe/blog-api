package com.example.blogapi.dto.request;

import com.example.blogapi.enums.PostSortField;
import org.springframework.data.domain.Sort;

public record PostSortRequest(
        PostSortField sortBy,
        Sort.Direction direction
) {
}

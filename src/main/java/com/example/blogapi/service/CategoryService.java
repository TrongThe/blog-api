package com.example.blogapi.service;


import com.example.blogapi.dto.request.CategoryCreateRequest;
import com.example.blogapi.dto.request.CategoryUpdateRequest;
import com.example.blogapi.dto.response.CategoryResponse;
import com.example.blogapi.entity.Category;
import com.example.blogapi.exception.ConflictException;
import com.example.blogapi.exception.ResourceNotFoundException;
import com.example.blogapi.mapper.CategoryMapper;
import com.example.blogapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @CacheEvict(value = "categories", key = "'all'")
    @Transactional
    public CategoryResponse create(CategoryCreateRequest request){

        String name = request.name().trim();

        if (categoryRepository.existsByNameIgnoreCase(name)){
            throw new ConflictException("Category already exists");
        }

        Category category = Category.builder()
                .name(request.name())
                .description(request.description())
                .build();

        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(savedCategory);
    }

    @Cacheable(value = "categories", key = "'all'")
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll(){
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse getById(Long id){

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        return categoryMapper.toResponse(category);
    }

    @CacheEvict(value = "categories", key = "'all'")
    @Transactional
    public CategoryResponse update(
            Long categoryId,
            CategoryUpdateRequest request
    ){

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        String name = request.name().trim();

        if (!category.getName().equalsIgnoreCase(name)
                && categoryRepository.existsByNameIgnoreCase(name)) {

            throw new ConflictException(
                    "Category already exists"
            );
        }

        category.setName(name);
        category.setDescription(request.description());

        return categoryMapper.toResponse(category);
    }

    @CacheEvict(value = "categories", key = "'all'")
    @Transactional
    public void delete(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        categoryRepository.delete(category);
    }

}

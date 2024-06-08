package com.ecomm.service.impl;

import com.ecomm.dto.CategoryDTO;
import com.ecomm.entity.Category;
import com.ecomm.exception.ResourceNotFoundException;
import com.ecomm.repository.CategoryRepository;
import com.ecomm.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            log.error("No categories found");
            throw new ResourceNotFoundException("No categories found");
        }
        log.info("Retrieved all categories successfully");
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        for (Category category : categories) {
            CategoryDTO categoryDTO = CategoryDTO.convertToDTO(category);
            categoryDTOList.add(categoryDTO);
        }
        return categoryDTOList;
    }

    @Override
    public CategoryDTO getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        log.info("Retrieved category with id: {}", categoryId);
        return CategoryDTO.convertToDTO(category);
    }
}

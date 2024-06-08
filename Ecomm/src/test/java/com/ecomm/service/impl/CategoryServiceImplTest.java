package com.ecomm.service.impl;

import com.ecomm.dto.CategoryDTO;
import com.ecomm.entity.Category;
import com.ecomm.entity.Product;
import com.ecomm.exception.ResourceNotFoundException;
import com.ecomm.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCategories() {
        Category category = new Category();
        category.setName("Test Category");
        List<Product> products = new ArrayList<>();
        category.setProducts(products);
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));

        assertThat(categoryService.getAllCategories()).hasSize(1);
    }

    @Test
    public void testGetAllCategories_NoCategories() {
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> categoryService.getAllCategories())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("No categories found");
    }

    @Test
    public void testGetCategoryById() {
        Category category = new Category();
        category.setName("Test Category");
        List<Product> products = new ArrayList<>();
        category.setProducts(products);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        CategoryDTO categoryDTO = categoryService.getCategoryById(1L);

        assertThat(categoryDTO.getName()).isEqualTo("Test Category");
    }

    @Test
    public void testGetCategoryById_CategoryNotFound() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getCategoryById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Category not found");
    }
}

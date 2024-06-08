package com.ecomm.service.impl;

import com.ecomm.dto.ProductDTO;
import com.ecomm.entity.Category;
import com.ecomm.entity.Product;
import com.ecomm.exception.ResourceNotFoundException;
import com.ecomm.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllProducts() {
        Product product = new Product();
        product.setName("Test Product");
        Category category = new Category();
        product.setCategory(category);
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

        assertThat(productService.getAllProducts()).hasSize(1);
    }

    @Test
    public void testGetAllProducts_NoProducts() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> productService.getAllProducts())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("No products found");
    }

    @Test
    public void testGetProductById() {
        Product product = new Product();
        product.setName("Test Product");
        Category category = new Category();
        product.setCategory(category);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        ProductDTO productDTO = productService.getProductById(1L);

        assertThat(productDTO.getName()).isEqualTo("Test Product");
    }

    @Test
    public void testGetProductById_ProductNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product with id 1 not found");
    }

    @Test
    public void testGetProductsByCategory() {
        Product product = new Product();
        product.setName("Test Product");
        Category category = new Category();
        product.setCategory(category);
        when(productRepository.findByCategoryId(anyLong())).thenReturn(Collections.singletonList(product));

        assertThat(productService.getProductsByCategory(1L)).hasSize(1);
    }

    @Test
    public void testGetProductsByCategory_NoProducts() {
        when(productRepository.findByCategoryId(anyLong())).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> productService.getProductsByCategory(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("No products found for this category");
    }
}

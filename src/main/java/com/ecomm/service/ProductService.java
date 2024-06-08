package com.ecomm.service;

import com.ecomm.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(Long productId);
    List<ProductDTO> getProductsByCategory(Long categoryId);
}

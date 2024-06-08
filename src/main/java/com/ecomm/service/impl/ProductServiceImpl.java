package com.ecomm.service.impl;

import com.ecomm.dto.ProductDTO;
import com.ecomm.entity.Product;
import com.ecomm.exception.ResourceNotFoundException;
import com.ecomm.repository.ProductRepository;
import com.ecomm.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductDTO> getAllProducts() {
        log.info("Fetching all products");
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            log.warn("No products found");
            throw new ResourceNotFoundException("No products found");
        }
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (Product product: products) {
            ProductDTO productDTO = ProductDTO.convertToDTO(product);
            productDTOList.add(productDTO);
        }
        log.info("Fetched all products successfully");
        return productDTOList;
    }

    @Override
    public ProductDTO getProductById(Long productId) {
        log.info("Fetching product with id: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product with id {} not found", productId);
                    return new ResourceNotFoundException("Product with id " + productId + " not found");
                });
        log.info("Fetched product with id: {}", productId);
        return ProductDTO.convertToDTO(product);
    }

    @Override
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        log.info("Fetching products by category id: {}", categoryId);
        List<Product> products = productRepository.findByCategoryId(categoryId);
        if (products.isEmpty()) {
            log.warn("No products found for category id: {}", categoryId);
            throw new ResourceNotFoundException("No products found for this category");
        }
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (Product product: products) {
            ProductDTO productDTO = ProductDTO.convertToDTO(product);
            productDTOList.add(productDTO);
        }
        log.info("Fetched products by category id: {} successfully", categoryId);
        return productDTOList;
    }
}

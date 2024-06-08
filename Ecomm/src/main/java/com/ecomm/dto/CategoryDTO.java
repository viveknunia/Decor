package com.ecomm.dto;

import com.ecomm.entity.Category;
import com.ecomm.entity.Product;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private List<ProductDTO> products;

    public static CategoryDTO convertToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (Product product : category.getProducts()) {
            ProductDTO productDTO = ProductDTO.convertToDTO(product);
            productDTOList.add(productDTO);
        }
        categoryDTO.setProducts(productDTOList);
        return categoryDTO;
    }
}

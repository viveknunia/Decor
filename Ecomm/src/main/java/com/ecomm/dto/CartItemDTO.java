package com.ecomm.dto;

import com.ecomm.entity.CartItem;
import lombok.Data;

@Data
public class CartItemDTO {
    private Long id;
    private Long cartId;
    private ProductDTO product;
    private double cartItemPrice;
    private int quantity;

    public static CartItemDTO convertToDTO(CartItem cartItem) {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setId(cartItem.getId());
        cartItemDTO.setCartId(cartItem.getCart().getId());
        cartItemDTO.setProduct(ProductDTO.convertToDTO(cartItem.getProduct()));
        cartItemDTO.setCartItemPrice(cartItem.getCartItemPrice());
        cartItemDTO.setQuantity(cartItem.getQuantity());
        return cartItemDTO;
    }
}

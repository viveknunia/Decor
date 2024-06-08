package com.ecomm.dto;

import com.ecomm.entity.Cart;
import com.ecomm.entity.CartItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartDTO {
    private Long id;
    private Long userId;
    private double cartTotalPrice;
    private List<CartItemDTO> cartItems;

    public static CartDTO convertToDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setUserId(cart.getUser().getId());
        cartDTO.setCartTotalPrice(cart.getCartTotalPrice());
        List<CartItemDTO> cartItemDTOS = new ArrayList<>();
        for(CartItem cartItem : cart.getCartItems()) {
            cartItemDTOS.add(CartItemDTO.convertToDTO(cartItem));
        }
        cartDTO.setCartItems(cartItemDTOS);
        return cartDTO;
    }
}

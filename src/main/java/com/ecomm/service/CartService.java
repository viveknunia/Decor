package com.ecomm.service;

import com.ecomm.dto.CartDTO;
import com.ecomm.dto.CartItemDTO;

public interface CartService {
    CartDTO getUserCart(Long userId);
    CartDTO addOrUpdateCart(CartItemDTO cartItemDTO);
    void clearCart(Long userId);
}

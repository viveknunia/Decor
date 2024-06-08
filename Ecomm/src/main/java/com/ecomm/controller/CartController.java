package com.ecomm.controller;

import com.ecomm.dto.CartDTO;
import com.ecomm.dto.CartItemDTO;
import com.ecomm.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/view/{userId}")
    public ResponseEntity<CartDTO> getUserCart(@PathVariable Long userId) {
        CartDTO cartDTO = cartService.getUserCart(userId);
        return ResponseEntity.ok(cartDTO);
    }

    @PostMapping
    public ResponseEntity<CartDTO> addOrUpdateCart(@RequestBody CartItemDTO cartItemDTO) {
        CartDTO cartDTO = cartService.addOrUpdateCart(cartItemDTO);
        return ResponseEntity.ok(cartDTO);
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared successfully");
    }
}

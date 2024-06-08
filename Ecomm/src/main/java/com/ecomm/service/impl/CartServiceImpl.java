package com.ecomm.service.impl;

import com.ecomm.dto.CartDTO;
import com.ecomm.dto.CartItemDTO;
import com.ecomm.entity.Cart;
import com.ecomm.entity.CartItem;
import com.ecomm.entity.Product;
import com.ecomm.entity.User;
import com.ecomm.exception.InsufficientQuantityException;
import com.ecomm.exception.InvalidStateException;
import com.ecomm.exception.ResourceNotFoundException;
import com.ecomm.repository.CartItemRepository;
import com.ecomm.repository.CartRepository;
import com.ecomm.repository.ProductRepository;
import com.ecomm.repository.UserRepository;
import com.ecomm.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public CartDTO getUserCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
        return CartDTO.convertToDTO(cart);
    }

    @Override
    public CartDTO addOrUpdateCart(CartItemDTO cartItemDTO) {
        Cart cart = cartRepository.findById(cartItemDTO.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        Product product = productRepository.findById(cartItemDTO.getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        if (product.getStockQuantity() < cartItemDTO.getQuantity()) {
            throw new InsufficientQuantityException("Insufficient stock for product");
        }
        Optional<CartItem> existingCartItemOptional = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(product.getId()))
                .findFirst();
        if (existingCartItemOptional.isPresent()) {
            CartItem existingCartItem = existingCartItemOptional.get();
            if (cartItemDTO.getQuantity() == 0) {
                cart.getCartItems().remove(existingCartItem);
                cartItemRepository.delete(existingCartItem);
            } else {
                existingCartItem.setQuantity(cartItemDTO.getQuantity());
                existingCartItem.setCartItemPrice(product.getPrice() * cartItemDTO.getQuantity());
                cartItemRepository.save(existingCartItem);
            }
        } else if (cartItemDTO.getQuantity() > 0) {
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(cartItemDTO.getQuantity());
            newCartItem.setCartItemPrice(product.getPrice() * newCartItem.getQuantity());
            cartItemRepository.save(newCartItem);
            cart.getCartItems().add(newCartItem);
        }
        double cartTotalPrice = 0.0;
        for (CartItem cartItem : cart.getCartItems()) {
            cartTotalPrice += cartItem.getCartItemPrice();
        }
        cart.setCartTotalPrice(cartTotalPrice);
        cartRepository.save(cart);
        return CartDTO.convertToDTO(cart);
    }

    @Override
    public void clearCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new InvalidStateException("No cart to clear"));
        if (!cart.getCartItems().isEmpty()) {
            cartItemRepository.deleteAllInBatch(cart.getCartItems());
            cart.getCartItems().clear();
        }
        cart.setCartTotalPrice(0.0);
        cartRepository.save(cart);
    }
}

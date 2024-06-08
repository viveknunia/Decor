package com.ecomm.service.impl;

import com.ecomm.dto.CartDTO;
import com.ecomm.dto.CartItemDTO;
import com.ecomm.dto.ProductDTO;
import com.ecomm.entity.*;
import com.ecomm.exception.InsufficientQuantityException;
import com.ecomm.exception.InvalidStateException;
import com.ecomm.exception.ResourceNotFoundException;
import com.ecomm.repository.CartItemRepository;
import com.ecomm.repository.CartRepository;
import com.ecomm.repository.ProductRepository;
import com.ecomm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserCart() {
        User user = new User();
        user.setId(1L);
        Cart cart = new Cart();
        cart.setUser(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(any(User.class))).thenReturn(Optional.of(cart));

        CartDTO cartDTO = cartService.getUserCart(1L);

        assertThat(cartDTO.getUserId()).isEqualTo(1L);
    }

    @Test
    public void testGetUserCart_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.getUserCart(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    public void testAddOrUpdateCart() {
        User user = new User();
        user.setId(1L);
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        Product product = new Product();
        product.setId(1L);
        product.setStockQuantity(10);
        Category category = new Category();
        category.setId(1L);
        product.setCategory(category);
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setCartId(1L);
        cartItemDTO.setQuantity(5);
        cartItemDTO.setProduct(ProductDTO.convertToDTO(product));
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        CartDTO cartDTO = cartService.addOrUpdateCart(cartItemDTO);

        assertThat(cartDTO.getId()).isEqualTo(1L);
    }

    @Test
    public void testAddOrUpdateCart_CartNotFound() {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setCartId(1L);
        when(cartRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.addOrUpdateCart(cartItemDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cart not found");
    }

    @Test
    public void testAddOrUpdateCart_ProductNotFound() {
        Cart cart = new Cart();
        cart.setId(1L);
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setCartId(1L);
        Product product = new Product();
        product.setId(1L);
        Category category = new Category();
        category.setId(1L);
        product.setCategory(category);
        cartItemDTO.setProduct(ProductDTO.convertToDTO(product));
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.addOrUpdateCart(cartItemDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product not found");
    }

    @Test
    public void testAddOrUpdateCart_InsufficientStock() {
        Cart cart = new Cart();
        cart.setId(1L);
        Product product = new Product();
        product.setId(1L);
        product.setStockQuantity(3);
        Category category = new Category();
        category.setId(1L);
        product.setCategory(category);
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setCartId(1L);
        cartItemDTO.setQuantity(5);
        cartItemDTO.setProduct(ProductDTO.convertToDTO(product));
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> cartService.addOrUpdateCart(cartItemDTO))
                .isInstanceOf(InsufficientQuantityException.class)
                .hasMessage("Insufficient stock for product");
    }

    @Test
    public void testClearCart() {
        User user = new User();
        user.setId(1L);
        Cart cart = new Cart();
        cart.setUser(user);
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cart.getCartItems().add(cartItem);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(any(User.class))).thenReturn(Optional.of(cart));

        cartService.clearCart(1L);

        verify(cartItemRepository, times(1)).deleteAllInBatch(any());
    }

    @Test
    public void testClearCart_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.clearCart(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    public void testClearCart_NoCartToClear() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(any(User.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.clearCart(1L))
                .isInstanceOf(InvalidStateException.class)
                .hasMessage("No cart to clear");
    }
}

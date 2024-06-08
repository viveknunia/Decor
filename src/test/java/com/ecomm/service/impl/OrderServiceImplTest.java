package com.ecomm.service.impl;

import com.ecomm.dto.OrderCheckoutDTO;
import com.ecomm.dto.CartDTO;
import com.ecomm.entity.*;
import com.ecomm.enums.OrderStatus;
import com.ecomm.exception.InsufficientQuantityException;
import com.ecomm.exception.ResourceNotFoundException;
import com.ecomm.repository.CartItemRepository;
import com.ecomm.repository.CartRepository;
import com.ecomm.repository.OrderRepository;
import com.ecomm.repository.ProductRepository;
import com.ecomm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPlaceOrder() {
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
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(5);
        cartItem.setCart(cart);
        cart.getCartItems().add(cartItem);
        OrderCheckoutDTO orderCheckoutDTO = new OrderCheckoutDTO();
        orderCheckoutDTO.setCart(CartDTO.convertToDTO(cart));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        orderService.placeOrder(orderCheckoutDTO);

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testPlaceOrder_UserNotFound() {
        OrderCheckoutDTO orderCheckoutDTO = new OrderCheckoutDTO();
        orderCheckoutDTO.setCart(new CartDTO());
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.placeOrder(orderCheckoutDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    public void testPlaceOrder_CartNotFound() {
        User user = new User();
        user.setId(1L);
        OrderCheckoutDTO orderCheckoutDTO = new OrderCheckoutDTO();
        CartDTO cartDTO = new CartDTO();
        cartDTO.setUserId(1L);
        orderCheckoutDTO.setCart(cartDTO);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(cartRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.placeOrder(orderCheckoutDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cart not found");
    }

    @Test
    public void testPlaceOrder_InsufficientStock() {
        User user = new User();
        user.setId(1L);
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        Product product = new Product();
        product.setId(1L);
        product.setStockQuantity(3);
        Category category = new Category();
        category.setId(1L);
        product.setCategory(category);
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(5);
        cartItem.setCart(cart);
        cart.getCartItems().add(cartItem);
        OrderCheckoutDTO orderCheckoutDTO = new OrderCheckoutDTO();
        orderCheckoutDTO.setCart(CartDTO.convertToDTO(cart));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> orderService.placeOrder(orderCheckoutDTO))
                .isInstanceOf(InsufficientQuantityException.class)
                .hasMessage("Insufficient quantity for product with id: " + product.getId());
    }

    @Test
    public void testGetOrdersByUser() {
        User user = new User();
        user.setId(1L);
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setOrderStatus(OrderStatus.CONFIRMED);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(orderRepository.findByUser(any(User.class))).thenReturn(Collections.singletonList(order));

        assertThat(orderService.getOrdersByUser(1L)).hasSize(1);
    }

    @Test
    public void testGetOrdersByUser_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrdersByUser(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    public void testGetOrdersByUser_NoOrders() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(orderRepository.findByUser(any(User.class))).thenReturn(Collections.emptyList());

        assertThat(orderService.getOrdersByUser(1L)).isEmpty();
    }
}

package com.ecomm.service.impl;

import com.ecomm.dto.OrderDTO;
import com.ecomm.dto.OrderCheckoutDTO;
import com.ecomm.entity.Cart;
import com.ecomm.entity.CartItem;
import com.ecomm.entity.Order;
import com.ecomm.entity.OrderItem;
import com.ecomm.entity.Product;
import com.ecomm.entity.User;
import com.ecomm.enums.OrderStatus;
import com.ecomm.exception.InsufficientQuantityException;
import com.ecomm.exception.ResourceNotFoundException;
import com.ecomm.repository.CartItemRepository;
import com.ecomm.repository.CartRepository;
import com.ecomm.repository.OrderRepository;
import com.ecomm.repository.ProductRepository;
import com.ecomm.repository.UserRepository;
import com.ecomm.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;


    @Override
    public void placeOrder(OrderCheckoutDTO orderCheckoutDTO) {
        User user = userRepository.findById(orderCheckoutDTO.getCart().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Cart cart = cartRepository.findById(orderCheckoutDTO.getCart().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setOrderTotalPrice(cart.getCartTotalPrice());
        order.setShippingAddress(orderCheckoutDTO.getShippingAddress());
        order.setPaymentMethod(orderCheckoutDTO.getPaymentMethod());
        order.setOrderStatus(OrderStatus.CONFIRMED);
        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();
            if (product.getStockQuantity() < quantity) {
                throw new InsufficientQuantityException("Insufficient quantity for product with id: " + product.getId());
            }
            product.setStockQuantity(product.getStockQuantity() - quantity);
            productRepository.save(product);
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setOrder(order);
            orderItem.setOrderItemPrice(cartItem.getCartItemPrice());
            order.getOrderItems().add(orderItem);
        }
        if (!cart.getCartItems().isEmpty()) {
            cartItemRepository.deleteAllInBatch(cart.getCartItems());
            cart.getCartItems().clear();
        }
        cart.setCartTotalPrice(0.0);
        cartRepository.save(cart);
        orderRepository.save(order);
    }

    @Override
    public List<OrderDTO> getOrdersByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Order> orders = orderRepository.findByUser(user);
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            orderDTOs.add(OrderDTO.convertToDTO(order));
        }
        return orderDTOs;
    }

}

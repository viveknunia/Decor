package com.ecomm.service;

import com.ecomm.dto.OrderDTO;
import com.ecomm.dto.OrderCheckoutDTO;

import java.util.List;

public interface OrderService {
    void placeOrder(OrderCheckoutDTO orderCheckoutDTO);
    List<OrderDTO> getOrdersByUser(Long userId);
}

package com.ecomm.dto;

import com.ecomm.entity.Order;
import com.ecomm.entity.OrderItem;
import com.ecomm.enums.OrderStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    private OrderStatus orderStatus;
    private String shippingAddress;
    private String paymentMethod;
    private double orderTotalPrice;
    private Date orderDate;
    private List<OrderItemDTO> orderItems;

    public static OrderDTO convertToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setUserId(order.getUser().getId());
        orderDTO.setOrderStatus(order.getOrderStatus());
        orderDTO.setShippingAddress(order.getShippingAddress());
        orderDTO.setPaymentMethod(order.getPaymentMethod());
        orderDTO.setOrderTotalPrice(order.getOrderTotalPrice());
        orderDTO.setOrderDate(order.getOrderDate());
        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
        for(OrderItem orderItem : order.getOrderItems()) {
            orderItemDTOS.add(OrderItemDTO.convertToDTO(orderItem));
        }
        orderDTO.setOrderItems(orderItemDTOS);
        return orderDTO;
    }
}

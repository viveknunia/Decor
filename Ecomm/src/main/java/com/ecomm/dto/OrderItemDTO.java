package com.ecomm.dto;

import com.ecomm.entity.OrderItem;
import lombok.Data;

@Data
public class OrderItemDTO {
    private Long id;
    private Long orderId;
    private ProductDTO product;
    private int quantity;
    private double orderItemPrice;

    public static OrderItemDTO convertToDTO(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(orderItem.getId());
        orderItemDTO.setOrderId(orderItem.getOrder().getId());
        orderItemDTO.setProduct(ProductDTO.convertToDTO(orderItem.getProduct()));
        orderItemDTO.setOrderItemPrice(orderItem.getOrderItemPrice());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        return orderItemDTO;
    }
}

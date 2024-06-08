package com.ecomm.dto;

import lombok.Data;

@Data
public class OrderCheckoutDTO {
    private CartDTO cart;
    private String shippingAddress;
    private String paymentMethod;
}

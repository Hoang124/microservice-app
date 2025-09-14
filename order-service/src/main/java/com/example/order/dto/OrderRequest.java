package com.example.order.dto;

import java.math.BigDecimal;

public record OrderRequest(Long id, String orderNumber, String skuCode,
                           BigDecimal price, Integer quantity, UserDetails userDetails) {

    public record UserDetails(String email, String firstName, String lastName) {}
//    public OrderRequest {
//        if (id == null || orderNumber == null || skuCode == null || price == null || quantity == null) {
//            throw new IllegalArgumentException("All fields must be provided");
//        }
//    }
}

package com.example.product.dto;

import java.math.BigDecimal;

public record ProductRequest(String name, String description, BigDecimal price) {
    // This record class represents the request body for creating a new product
    // It contains fields for the product's name, description, and price
    // The record will automatically generate getters and a constructor
}
